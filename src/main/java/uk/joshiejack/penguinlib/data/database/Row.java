package uk.joshiejack.penguinlib.data.database;

import com.google.common.collect.Maps;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.data.holder.HolderItem;
import uk.joshiejack.penguinlib.util.Patterns;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

@SuppressWarnings("null")
public class Row {
    public static final Row EMPTY = new Row(Table.EMPTY, new String[0], new String[0]);
    private final Map<String, Object> data = Maps.newHashMap();
    private final Table table;

    Row(Table table, String[] labelset, String[] dataset) {
        try {
            for (int i = 0; i < labelset.length; i++) {
                set(labelset[i], dataset[i]);
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            PenguinLib.logger.log(Level.ERROR, "Failed to set the values for the label set: " + Arrays.toString(labelset) +
                                                " with the data " + Arrays.toString(dataset));
            throw(ex);
        }

        this.table = table;
    }

    public String get() {
        return (String) data.values().stream().findFirst().get();
    }


    @SuppressWarnings("unchecked")
    public <T> T get(String label) {
        return (T) data.get(label.toLowerCase(Locale.ENGLISH));
    }

    public int getTime(String name) {
        String entry = get(name).toString();
        if (!entry.contains(":")) return -1;
        String[] split = get(name).toString().split(":");
        int hours = Integer.parseInt(split[0]);
        int minutes = Integer.parseInt(split[1]);
        int seconds = split.length > 2 ? Integer.parseInt(split[2]) : 0;
        int totalSeconds = (hours * 3600) + (minutes * 60) + seconds;
        return (int) (24000 / (86400D / totalSeconds));
    }

    public ResourceLocation getScript() {
        return new ResourceLocation(get("script").toString().replace("/", "_")); //Convert to namesake
    }

    public String id() {
        return get("id");
    }

    public ItemStack icon() {
        ItemStack icon = StackHelper.getStackFromString(get("icon"));
        return icon.isEmpty() ? new ItemStack(Items.POTATO) : icon;
    }

    public Holder holder() {
        if (isEmpty("item")) return HolderItem.EMPTY;
        return Holder.getFromString(get("item"));
    }

    public ItemStack item() {
        return StackHelper.getStackFromString(get("item"));
    }

    public float getAsFloat(String label) {
        return Float.parseFloat(get(label).toString());
    }

    public long getAsLong(String label) {
        return Long.parseLong(get(label).toString().trim());
    }

    public double getAsDouble(String label) {
        return Double.parseDouble(get(label).toString().trim());
    }

    public int getAsInt(String label) {
        return Integer.parseInt(get(label).toString());
    }

    public int getColor(String label) {
        return Integer.parseInt(get(label).toString().replace("0x", "").replace("#", ""), 16);
    }

    public boolean isEmpty(String label) {
        if (get(label) == null) return true;
        String name = get(label).toString();
        return name.isEmpty() || name.equals("none") || name.equals("default");
    }

    @Override
    public String toString() {
        return Arrays.toString(data.values().toArray());
    }

    //Search the objects for a match
    public boolean contains(String match) {
        for (Object object: data.values()) {
            if (object instanceof String) {
                if(match.equals(object)) return true;
            }
        }

        return false;
    }

    //Call to set the values, pulled in from the csvs
    public void set(String label, String value) {
        if (Patterns.BOOLEAN_PATTERN.matcher(value).matches()) data.put(label, Boolean.valueOf(value));
        else if (Patterns.DOUBLE_PATTERN.matcher(value).matches()) data.put(label, Double.valueOf(value));
        else if (Patterns.INTEGER_PATTERN.matcher(value).matches()) data.put(label, Integer.valueOf(value));
        else data.put(label, value);
    }

    public String name() {
        return get("name");
    }

    public boolean isTrue(String name) {
        Object ret = get(name);
        return ret instanceof Boolean ? (boolean) ret : Boolean.parseBoolean(ret.toString());
    }

}
