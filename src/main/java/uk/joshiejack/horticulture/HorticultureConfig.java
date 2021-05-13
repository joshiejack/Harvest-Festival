package uk.joshiejack.horticulture;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.helpers.forge.ConfigHelper;

import static uk.joshiejack.horticulture.Horticulture.MODID;

@SuppressWarnings({"CanBeFinal"})
@Mod.EventBusSubscriber(modid = Horticulture.MODID)
@Config(modid = MODID)
public class HorticultureConfig {
    @Config.Comment("Add recipes for all the drinks and meals")
    public static boolean addRecipes = true;
    @Config.Comment("Watering can applies bonemeal effect")
    public static boolean bonemealWateringCan = true;
    @Config.Comment("Enable watering can recipe")
    public static boolean recipeWateringCan = true;
    @Config.Comment("Fruit tree leaves generate fruit")
    public static boolean leavesGenerateFruit = true;
    @Config.Comment("Fruit growth chance, 0 > 1 value")
    public static double fruitGrowthChance = 0.05;
    @Config.Comment("Allow certain crops to regrow when right clicked")
    public static boolean enableCropRegrowth = true;
    @Config.Comment("Allow horticulture crops to be harvested with right click")
    public static boolean enableRightClickHarvest = true;
    @Config.Comment("Enable turnip, cucumber, onion, tomato, spinach and eggplant in grass drops")
    public static boolean enableGrassDrops = true;
    @Config.Comment("Enable strawberry, corn, sweet potato, orange and peach in dungeon chests")
    public static boolean enableDungeonChests = true;
    @Config.Comment("Enable cabbage, pineapple, green pepper and grape trade with farming villager")
    public static boolean enableVillagerTrades = true;
    @Config.Comment("Enable banana trees in jungles")
    public static boolean enableBananaTrees = true;
    @Config.Comment("Enable apple trees in forests")
    public static boolean enableAppleTrees = true;
    @Config.Comment("Crops drop seeds")
    public static boolean enableSeedDrops = false;
    @Config.Comment("Enable seed maker recipe")
    public static boolean enableSeedMakerRecipe = true;
    @Config.Comment("Enable sprinkler recipe")
    public static boolean enableSprinklerRecipe = true;
    @Config.Comment("Enable mushroom log recipe")
    public static boolean enableMushroomLogRecipe = true;

    @SubscribeEvent
    public static void onConfigLoaded(DatabaseLoadedEvent.ConfigLoad event) throws IllegalAccessException {
        ConfigHelper.readFromDatabase(event, Horticulture.MODID, HorticultureConfig.class);
    }
}
