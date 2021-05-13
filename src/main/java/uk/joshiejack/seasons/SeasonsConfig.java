package uk.joshiejack.seasons;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.helpers.forge.ConfigHelper;

@Mod.EventBusSubscriber(modid = Seasons.MODID)
@Config(modid = Seasons.MODID)
public class SeasonsConfig {
    @Config.Comment("Weather will only change at 6AM if true")
    public static boolean dailyWeather = false;
    @Config.Comment("Days per season multiplier")
    public static int daysPerSeasonMultiplier = 1;
    @Config.Comment("New dimension for overworld id")
    public static int overworldID = 3;
    @Config.Comment("Enable 24h Clock")
    public static boolean clock24H = true;
    @Config.Comment("Seasonal crop growth")
    public static boolean seasonalCropGrowth;

    @SubscribeEvent
    public static void onConfigLoaded(DatabaseLoadedEvent.ConfigLoad event) throws IllegalAccessException {
        ConfigHelper.readFromDatabase(event, Seasons.MODID, SeasonsConfig.class);
    }
}
