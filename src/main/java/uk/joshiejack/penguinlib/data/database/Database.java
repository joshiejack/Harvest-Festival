package uk.joshiejack.penguinlib.data.database;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.util.helpers.minecraft.ResourceLoader;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Database {
    private final Map<String, Table> tables = Maps.newHashMap();

    @Nonnull
    public Table table(String table) {
        return tables.getOrDefault(table, Table.EMPTY);
    }

    public void print() {
        for (String table: tables.keySet()) {
            PenguinLib.logger.info("############## TABLE: " + table +   " ##############");
            PenguinLib.logger.info(tables.get(table).labelset());

            tables.get(table).rows().forEach(r -> {
                List<String> arr = new ArrayList<>();
                tables.get(table).labels().forEach(header -> arr.add(r.get(header)));
                PenguinLib.logger.info(Arrays.toString(arr.toArray()));
            });
        }
    }

    @Nonnull
    public <T> T get(String search) {
        String[] terms = search.split(",");
        String table = terms[0].trim();
        String row = terms[1].trim();
        String data = terms[2].trim();
        Preconditions.checkNotNull(table, "The table cannot be null: " + table);
        Preconditions.checkNotNull(row, "The id to search in the table cannot be null: " + row);
        Preconditions.checkNotNull(data, "The instance you are searching for cannot be null: " + data);
        return table(table).find(row).get(data);
    }

    public Table createTable(String name, String... labelset) {
        if (tables.containsKey(name)) return tables.get(name);
        else {
            Table table = new Table(name, labelset);
            tables.put(name, table);
            return table;
        }
    }

    private void parseCSV(String name, String csv) {
        //Ignore any directories registered for this csv and just go with the name
        String file_name = new File(name).getName();
        name = file_name.startsWith("$") ? file_name.replace("$", "") : file_name.contains("$") ? file_name.split("\\$")[1] : file_name; //Ignore anything before the dollar symbol
        String[] entries = csv.split("[\\r\\n]+");
        String[] labels = entries[0].split(",");
        Table table = createTable(name, labels); //Get a table with this name if it already exists
        for (int i = 1; i < entries.length; i++) {
            if (!entries[i].startsWith("#") && !entries[i].isEmpty()) {
                List<String> list = CSVUtils.parse(entries[i]);
                if (!list.isEmpty()) {
                    try {
                        table.insert(list.toArray(new String[list.size()]));
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        PenguinLib.logger.log(Level.ERROR, "Failed to insert the csv: " + name + " as there was an issue on line: " + i + " " + entries[i]);
                    }
                }
            }
        }
    }

    /**
     * Call this to load the database
     *
     * @return this */
    public Database load(String directory) {
        //Create the database config directory
        File database = new File(PenguinLib.getDirectory(), directory);
        if (!database.exists()) database.mkdir();
        //Add the resources and files to the database
        FileUtils.listFiles(database, new String[]{"csv"}, true)
                .forEach(csv -> parseCSV(csv.getName().replace(".csv", ""), ResourceLoader.getStringFromFile(csv)));
        ResourceLoader.get().getResourceListInDirectory(directory, "csv")
                .forEach(csv -> parseCSV(csv.getPath(), ResourceLoader.getDatabaseResource(csv, directory)));
        return this;
    }

    public static boolean[] toBooleanArray(Object row) {
        String pattern = row.toString().replace(" ", ""); //Remove whitespace
        String[] parts = pattern.split(",");
        boolean[] array = new boolean[parts.length];
        for (int i = 0; i < parts.length; i++)
            array[i] = Boolean.parseBoolean(parts[i]);
        return array;
    }

    public static class DatabaseException extends NullPointerException {
        public DatabaseException(String string) {
            super(string);
        }
    }
}