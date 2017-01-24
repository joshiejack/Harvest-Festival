package joshie.harvest.calendar;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.SeasonProvider;
import joshie.harvest.calendar.provider.HFWorldProvider;
import joshie.harvest.calendar.provider.SeasonProviderHidden;
import joshie.harvest.core.helpers.ConfigHelper;
import joshie.harvest.core.util.annotations.HFLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;

import static joshie.harvest.core.helpers.ConfigHelper.*;
import static joshie.harvest.core.lib.LoadOrder.HFCALENDAR;

@HFLoader(priority = HFCALENDAR)
public class HFCalendar {
    private static final SeasonProvider HIDDEN = new SeasonProviderHidden();
    public static Configuration CONFIG;
    public static int DAYS_PER_SEASON_INTEGRATED;
    private static int DAYS_PER_SEASON_DEDICATED;
    public static long TICKS_PER_DAY;
    public static long TWO_HOURS;
    public static boolean ENABLE_SUNNY;
    public static boolean ENABLE_RAIN;
    public static boolean ENABLE_TYPHOON;
    public static boolean ENABLE_SNOW;
    public static boolean ENABLE_BLIZZARD;
    public static boolean ENABLE_DATE_HUD;
    public static boolean ENABLE_GOLD_HUD;
    public static boolean SNOW_TICKER;
    public static int OVERWORLD_ID;
    public static boolean HIDE_CALENDAR_TEXTURE;
    public static boolean HIDE_GOLD_TEXTURE;
    public static int X_CALENDAR;
    public static int Y_CALENDAR;
    public static int X_GOLD;
    public static int Y_GOLD;
    public static boolean CLOCK_24H;

    public static void preInit() {
        DimensionType seasons = DimensionType.register("seasons", "seasons", OVERWORLD_ID, HFWorldProvider.class, true);
        DimensionManager.unregisterDimension(0);
        DimensionManager.registerDimension(0, seasons);
        HFApi.calendar.registerSeasonProvider(1, HIDDEN);
        HFApi.calendar.registerSeasonProvider(-1, HIDDEN);
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
        DAYS_PER_SEASON_INTEGRATED = getInteger("Integrated Server > Days per season", 30);
        DAYS_PER_SEASON_DEDICATED = getInteger("Dedicated Server > Days per season", 365);
        TICKS_PER_DAY = getInteger("Ticks per day", 24000);
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
        SNOW_TICKER = getBoolean("Remove snow faster", true);
        CLOCK_24H = getBoolean("24 hour clock", true);
        TWO_HOURS = (TICKS_PER_DAY / 12);
    }

    public static void onServerStarting() {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server.isDedicatedServer()) {
            CalendarDate.DAYS_PER_SEASON = DAYS_PER_SEASON_DEDICATED;
        } else CalendarDate.DAYS_PER_SEASON = DAYS_PER_SEASON_INTEGRATED;
    }
}
