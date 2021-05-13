package uk.joshiejack.husbandry;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.helpers.forge.ConfigHelper;

import static uk.joshiejack.husbandry.Husbandry.MODID;

@SuppressWarnings("CanBeFinal")
@Mod.EventBusSubscriber(modid = Husbandry.MODID)
@Config(modid = MODID)
public class HusbandryConfig {
    @Config.Comment("Number of days per year, used for determining animal lifespans")
    public static int daysPerYear = 120;
    @Config.Comment("Chance of death each day after minimum age, 1 in X cost")
    public static int deathChance = 360;
    @Config.Comment("Animals gain happiness from being in the sun")
    public static boolean happySun = true;
    @Config.Comment("Enable tool recipes")
    public static boolean enableToolRecipes = true;
    @Config.Comment("Enable food recipes")
    public static boolean enableFoodRecipes = true;
    @Config.Comment("Enable feed recipes")
    public static boolean enableFeedRecipes = true;
    @Config.Comment("Enable machine recipes")
    public static boolean enableMachineRecipes = true;
    @Config.Comment("Enable specialised treat recipes")
    public static boolean enableTreatRecipes = false;
    @Config.Comment("Enable generic treat recipe")
    public static boolean enableGenericTreatRecipe = false;
    @Config.Comment("Enable treat trades with shepherd villager")
    public static boolean enableTreatTrades = true;
    @Config.Comment("Enable treat in loot chests")
    public static boolean enableLootTreats = true;
    @Config.Comment("Enable feeder recipes")
    public static boolean enableFeederRecipes = true;
    @Config.Comment("Enable nest recipes")
    public static boolean enableNestRecipe = true;
    @Config.Comment("Enable No Damage on Sickle")
    public static boolean enableNoDamageSickle = false;

    @SubscribeEvent
    public static void onConfigLoaded(DatabaseLoadedEvent.ConfigLoad event) throws IllegalAccessException {
        ConfigHelper.readFromDatabase(event, Husbandry.MODID, HusbandryConfig.class);
    }
}
