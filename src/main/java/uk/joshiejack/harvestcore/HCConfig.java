package uk.joshiejack.harvestcore;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.helpers.forge.ConfigHelper;

@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
@Config(modid = HarvestCore.MODID)
public class HCConfig {
    @Config.Comment("Enable spring wilderness crops")
    public static boolean enableSpringWildernessCrops = false;
    @Config.Comment("Enable summer wilderness crops")
    public static boolean enableSummerWildernessCrops = false;
    @Config.Comment("Enable autumn wilderness crops")
    public static boolean enableAutumnWildernessCrops = false;
    @Config.Comment("Enable winter wilderness crops")
    public static boolean enableWinterWildernessCrops = false;
    @Config.Comment("Enable wet season wilderness crops")
    public static boolean enableWetWildernessCrops = false;
    @Config.Comment("Enable dry season wilderness crops")
    public static boolean enableDryWildernessCrops = false;
    @Config.Comment("Minimum wilderness distance")
    public static int minWildernessDistance = 16;
    @Config.Comment("Maximum wilderness spawns")
    public static int maxWildernessSpawns = 128;
    @Config.Comment("Maximum wilderness distance")
    public static int maxWildernessDistance = 128;
    @Config.Comment("Vanilla villages will generate wilderness items around them")
    public static boolean vanillaVillagesGenerateWilderness = true;
    @Config.Comment("Disabling this will disable the wilderness spawns generating at all")
    public static boolean enableWildernessSpawns = true;
    @Config.Comment("Disable vanilla villages")
    public static boolean disableVanillaVillages = false;
    @Config.Comment("Disables natural monster spawns above this y value in the overworld, set to 0 or to disable monsters spawns, or 256 for normal spawning")
    public static int mobSpawnsMaxHeight = 256;
    @Config.Comment("Disable animal natural spawns in the overworld")
    public static boolean disableOverworldAnimals = false;
    @Config.Comment("Set this to 1 to enable 3x3 seeds")
    public static int seedRange = 0;

    @SubscribeEvent
    public static void onConfigLoaded(DatabaseLoadedEvent.ConfigLoad event) throws IllegalAccessException {
        ConfigHelper.readFromDatabase(event, HarvestCore.MODID, HCConfig.class);
    }
}
