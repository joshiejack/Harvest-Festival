package joshie.harvest.calendar;

import static joshie.harvest.core.helpers.generic.ConfigHelper.getBoolean;
import static joshie.harvest.core.helpers.generic.ConfigHelper.getInteger;

public class HFCalendar {
    public static int DAYS_PER_SEASON;
    public static long TICKS_PER_DAY;
    public static boolean ENABLE_DAY_LENGTH;
    public static boolean ENABLE_SEASONAL_SKY;
    public static boolean ENABLE_FORECAST;
    public static boolean ENABLE_SUNNY;
    public static boolean ENABLE_RAIN;
    public static boolean ENABLE_TYPHOON;
    public static boolean ENABLE_SNOW;
    public static boolean ENABLE_BLIZZARD;
    public static boolean ENABLE_DATE_HUD;
    public static boolean ENABLE_GOLD_HUD;
    public static boolean ENABLE_HUD_XZ;
    public static int HUD_XSTART;
    public static int HUD_ZSTART;
    public static int HUD_XEND;
    public static int HUD_ZEND;
    public static int HUD_DIMENSION;

    public static void init() {
        DAYS_PER_SEASON = getInteger("Days per Season", 30);
        TICKS_PER_DAY = getInteger("Ticks per Day", 24000);
        ENABLE_DAY_LENGTH = getBoolean("Day Length > Enable Difference per Season", true);
        ENABLE_SEASONAL_SKY = getBoolean("Sky > Enable Difference per Season", true);
        ENABLE_FORECAST = getBoolean("Weather > Enable Daily System", true);
        ENABLE_SUNNY = getBoolean("Weather > Enable Sunny", true);
        ENABLE_RAIN = getBoolean("Weather > Enable Rain", true);
        ENABLE_TYPHOON = getBoolean("Weather > Enable Typhoon", true);
        ENABLE_SNOW = getBoolean("Weather > Enable Snow", true);
        ENABLE_BLIZZARD = getBoolean("Weather > Enable Blizzard", true);
        ENABLE_DATE_HUD = getBoolean("HUD > Enable Data", true);
        ENABLE_GOLD_HUD = getBoolean("HUD > Enable Gold", true);
        ENABLE_HUD_XZ = getBoolean("HUD > Coordinates", false);
        if (ENABLE_HUD_XZ) {
            ENABLE_DATE_HUD = true;
            ENABLE_GOLD_HUD = true;
            HUD_DIMENSION = getInteger("HUD > Dimension", 0);
            HUD_XSTART = getInteger("HUD > XStart", 0, "This number must be lower than XEnd");
            HUD_ZSTART = getInteger("HUD > ZStart", 0, "This number must be lower than ZEnd");
            HUD_XEND = getInteger("HUD > XEnd", 100, "This number must be higher than XStart");
            HUD_ZEND = getInteger("HUD > ZEnd", 100, "This number must be higher than ZStart");
        }
    }
}
