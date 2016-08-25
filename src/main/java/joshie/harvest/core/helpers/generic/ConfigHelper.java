package joshie.harvest.core.helpers.generic;

import net.minecraftforge.common.config.Configuration;

public class ConfigHelper {
    private static Configuration config;
    private static String category = "General";

    public static Configuration getConfig() {
        return config;
    }

    public static void setConfig(Configuration config) {
        ConfigHelper.config = config;
    }

    public static void setCategory(String category) {
        ConfigHelper.category = category;
    }

    public static boolean getBoolean(String name, boolean dft) {
        return config.get(category, name, dft).getBoolean(dft);
    }

    public static boolean getBoolean(String name, boolean dft, String comment) {
        return config.get(category, name, dft, comment).getBoolean(dft);
    }

    public static int getInteger(String name, int dft) {
        return config.get(category, name, dft).getInt();
    }
    
    public static int getInteger(String name, int dft, String comment) {
        return config.get(category, name, dft, comment).getInt();
    }
    
    public static double getDouble(String name, double dft) {
        return config.get(category, name, dft).getDouble();
    }

    public static long getLong(String name, long dft) {
        return Long.parseLong(config.get(name, name, "" + dft).getString());
    }

    public static void setInteger(String name, int dft) {
        config.get(category, name, dft).set(dft);
    }

    public static void setBoolean(String name, boolean dft) {
        config.get(category, name, dft).set(dft);
    }
}
