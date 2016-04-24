package joshie.harvest.core.config;

import net.minecraftforge.common.config.Configuration;

import static joshie.harvest.core.helpers.generic.ConfigHelper.getBoolean;
import static joshie.harvest.core.helpers.generic.ConfigHelper.getInteger;

public class Calendar {
    public static int DAYS_PER_SEASON;
    public static long TICKS_PER_DAY;
    public static boolean ENABLE_SUNNY;
    public static boolean ENABLE_RAIN;
    public static boolean ENABLE_TYPHOON;
    public static boolean ENABLE_SNOW;
    public static boolean ENABLE_BLIZZARD;

    public static void init(Configuration config) {
        DAYS_PER_SEASON = getInteger("Days per Season", 30);
        TICKS_PER_DAY = getInteger("Ticks per Day", 24000);
        ENABLE_SUNNY = getBoolean("Weather > Enable Sunny", true);
        ENABLE_RAIN = getBoolean("Weather > Enable Rain", true);
        ENABLE_TYPHOON = getBoolean("Weather > Enable Typhoon", true);
        ENABLE_SNOW = getBoolean("Weather > Enable Snow", true);
        ENABLE_BLIZZARD = getBoolean("Weather > Enable Blizzard", true);
    }
}
