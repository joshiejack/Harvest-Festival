package uk.joshiejack.penguinlib.data.database;

import java.util.*;
import java.util.stream.Collectors;

public class Table {
    public static final Table EMPTY = new Table("EMPTY");
    private final String name;
    private List<Row> data = new LinkedList<>();
    private String[] labelset;

    public Table(String name, String... labelset) {
        this.name = name;
        this.labelset = new String[labelset.length];
        for (int i = 0; i < labelset.length; i++) {
            this.labelset[i] = labelset[i].toLowerCase(Locale.ENGLISH);
        }
    }

    public void clear() {
        data.clear(); //Clear out all the data
    }

    public List<String> labels() {
        return Arrays.stream(labelset).collect(Collectors.toList());
    }

    public Collection<Row> rows() {
        return data;
    }

    public void insert(Object... dataset) {
        String[] data = new String[dataset.length];
        for (int i = 0; i < data.length; i++) {
            data[i] = String.valueOf(dataset[i]);
        }

        this.insert(data);
    }

    public String labelset() {
        return Arrays.toString(labelset);
    }

    public void insert(String... dataset) {
        data.add(new Row(this, labelset, dataset));
    }

    public Row find(String searchparameter) {
        return data.stream().filter(r -> r.contains(searchparameter)).findFirst().get();
    }

    public Row fetch_where(String query) {
        List<Row> ret = where(query);
        return ret.size() == 0 ? Row.EMPTY : ret.get(0);
    }

    public List<Row> where(String query) {
        if (query.equals("*")) return data;
        else if (query.contains("&")) {
            String[] queries = query.split("&");
            return data.stream().filter(r -> {
                int match_count = 0;
                for (String s : queries) {
                    String field = s.split("=")[0];
                    String search = s.split("=")[1];
                    if (r.get(field).equals(search)){
                        match_count++;
                    }
                }

                return match_count == queries.length;
            }).collect(Collectors.toList());
        } else if (query.contains("|")) {
            String[] queries = query.split("\\|");
            return data.stream().filter(r -> {
                for (String s : queries) {
                    String field = s.split("=")[0];
                    String search = s.split("=")[1];
                    if (r.get(field).equals(search)) {
                        return true;
                    }
                }

                return false;
            }).collect(Collectors.toList());
        } else {
            String field = query.split("=")[0];
            String search = query.split("=")[1];
            return data.stream().filter(r -> r.get(field).equals(search)).collect(Collectors.toList());
        }
    }

    public String name() {
        return name;
    }
}
