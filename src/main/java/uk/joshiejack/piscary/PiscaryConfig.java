package uk.joshiejack.piscary;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.helpers.forge.ConfigHelper;

@Mod.EventBusSubscriber(modid = Piscary.MODID)
@Config(modid = Piscary.MODID)
public class PiscaryConfig {
    @Config.Comment("Add recipes for all the drinks, meals and dinnerware")
    public static boolean addRecipes = true;
    @Config.Comment("Enable random items for pirate treasure")
    public static boolean enableTreasure = true;
    @Config.Comment("Enable fish to be converted to vanilla equivalents")
    public static boolean convertableFish = true;
    @Config.Comment("Enable fishing rod recipe")
    public static boolean enableFishingRodRecipe = true;
    @Config.Comment("Enable fish trap recipe")
    public static boolean enableTrapRecipe = true;
    @Config.Comment("Enable hatchery recipe")
    public static boolean enableHatcheryRecipe = true;
    @Config.Comment("Enable the recipes for bait")
    public static boolean enableBaitRecipes = true;
    @Config.Comment("Enable the recycler recipe")
    public static boolean enableRecyclerRecipe = true;
    @Config.Comment("Enable no dame on Fishing Rod")
    public static boolean enableNoDamageFishingRod = false;

    @SubscribeEvent
    public static void onConfigLoaded(DatabaseLoadedEvent.ConfigLoad event) throws IllegalAccessException {
        ConfigHelper.readFromDatabase(event, Piscary.MODID, PiscaryConfig.class);
    }
}
