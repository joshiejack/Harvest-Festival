package uk.joshiejack.gastronomy;

import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.helpers.forge.ConfigHelper;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@Mod.EventBusSubscriber(modid = Gastronomy.MODID)
@Config(modid = Gastronomy.MODID)
public class GastronomyConfig {
    @Config.Comment("Add recipes for the kitchen utensils")
    public static boolean addKitchenRecipes = true;
    @Config.Comment("With this as true you will find recipes in chests or for trade from villagers or other sources. With it false you will know all recipes right away.")
    public static boolean findRecipes = true;
    @Config.Comment("The cookbook will start with all the vanilla food recipes in the book already.")
    public static boolean vanillaRecipes = true;
    @Config.Comment("Enable the world gen of herbs, matsutake and bamboo")
    public static boolean enableWorldGen = true;
    @Config.Comment("Require the dish be held in order to take out the meal")
    public static boolean enableDishRequirement = true;
    @Config.Comment("Vanilla recipes and cooking will be disabled")
    public static boolean disableVanillaFoodRecipes = true;

    @SubscribeEvent
    public static void onConfigLoaded(DatabaseLoadedEvent.ConfigLoad event) throws IllegalAccessException {
        ConfigHelper.readFromDatabase(event, Gastronomy.MODID, GastronomyConfig.class);
    }
}
