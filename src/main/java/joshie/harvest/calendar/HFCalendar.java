package joshie.harvest.calendar;

import joshie.harvest.core.helpers.generic.ConfigHelper;
import joshie.harvest.core.util.HFLoader;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.Configuration;

import static joshie.harvest.core.helpers.generic.ConfigHelper.*;
import static joshie.harvest.core.lib.LoadOrder.HFCALENDAR;

@HFLoader(priority = HFCALENDAR)
public class HFCalendar {
    public static Configuration CONFIG;
    private static DimensionType SEASONS;
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
    public static int OVERWORLD_ID;
    public static boolean HIDE_CALENDAR_TEXTURE;
    public static boolean HIDE_GOLD_TEXTURE;
    public static int X_CALENDAR;
    public static int Y_CALENDAR;
    public static int X_GOLD;
    public static int Y_GOLD;

    public static void preInit() {
        SEASONS = DimensionType.register("seasons", "seasons", OVERWORLD_ID, WeatherProvider.class, true);
        DimensionManager.unregisterDimension(0);
        DimensionManager.registerDimension(0, SEASONS);
    }

    public static void save() {
        ConfigHelper.setConfig(CONFIG);
        ConfigHelper.setCategory("calendar");
        setInteger("HUD > Calendar X", X_CALENDAR);
        setInteger("HUD > Calendar Y", Y_CALENDAR);
        setBoolean("HUD > Calendar Hide Texture", HIDE_CALENDAR_TEXTURE);
        setInteger("HUD > Gold X", X_GOLD);
        setInteger("HUD > Gold Y", Y_GOLD);
        setBoolean("HUD > Gold Hide Texture", HIDE_GOLD_TEXTURE);
        CONFIG.save();
    }

    //Configuration
    public static void configure() {
        CONFIG = ConfigHelper.getConfig();
        OVERWORLD_ID = getInteger("Overworld ID", 3);
        DAYS_PER_SEASON = getInteger("Days per season", 30);
        TICKS_PER_DAY = getInteger("Ticks per day", 24000);
        ENABLE_DAY_LENGTH = getBoolean("Seasons > Different day length", true);
        ENABLE_SEASONAL_SKY = getBoolean("Seasons > Different sky appearance", true);
        ENABLE_FORECAST = getBoolean("Weather > Enable weather system", true);
        ENABLE_SUNNY = getBoolean("Weather > Enable sunny", true);
        ENABLE_RAIN = getBoolean("Weather > Enable rain", true);
        ENABLE_TYPHOON = getBoolean("Weather > Enable typhoon", true);
        ENABLE_SNOW = getBoolean("Weather > Enable snow", true);
        ENABLE_BLIZZARD = getBoolean("Weather > Enable blizzard", true);
        HIDE_CALENDAR_TEXTURE = getBoolean("HUD > Calendar Hide Texture", false);
        X_CALENDAR = getInteger("HUD > Calendar X", 0);
        Y_CALENDAR = getInteger("HUD > Calendar Y", 0);
        HIDE_GOLD_TEXTURE = getBoolean("HUD > Gold Hide Texture", false);
        X_GOLD = getInteger("HUD > Gold X", 0);
        Y_GOLD = getInteger("HUD > Gold Y", 0);
        ENABLE_DATE_HUD = getBoolean("HUD > Enable data", true);
        ENABLE_GOLD_HUD = getBoolean("HUD > Enable gold", true);
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
