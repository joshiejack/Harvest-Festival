package joshie.harvest.debug;

import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.core.util.annotations.HFLoader;
import net.minecraft.util.ResourceLocation;

import java.io.PrintWriter;

import static joshie.harvest.core.lib.HFModInfo.MODID;

@HFLoader
public class Debug {
    private static void register(String name, String description) {
        Recipe recipe = Recipe.REGISTRY.getValue(new ResourceLocation(MODID, name));
        CommandExportRecipe.DESCRIPTIONS.put(recipe, description);
    }

    private static void registerOverride(String name, String description) {
        Recipe recipe = Recipe.REGISTRY.getValue(new ResourceLocation(MODID, name));
        CommandExportRecipe.INFO_OVERRIDE.put(recipe, description);
    }

    private static void addFryingPanRecipe(Meal meal, String description) {
        register(meal.name().toLowerCase(), description);
    }

    private static void addMixerRecipe(Meal meal, String description) {
        register(meal.name().toLowerCase(), description);
    }

    private static void addNoUtensilRecipe(Meal meal, String description) {
        register(meal.name().toLowerCase(), description);
    }

    private static void addPotRecipe(Meal meal, String description) {
        register(meal.name().toLowerCase(), description);
    }

    private static void addOvenRecipe(Meal meal, String description) {
        register(meal.name().toLowerCase(), description);
    }

    public static void postInit() {
        addFryingPanRecipe(Meal.PANCAKE_SAVOURY, "10,000RP with [[Jim]]"); //Jim 10000RP
        addFryingPanRecipe(Meal.FRIES_FRENCH, "10,000RP with [[Johan]]"); //Girafi 10000RP
        addFryingPanRecipe(Meal.POPCORN, "10,000RP with [[Ashlee]]"); //Ashlee 10000RP
        addFryingPanRecipe(Meal.CORNFLAKES, "[[Cafe]] on Sundays in [[Summer]]"); //Shop
        addFryingPanRecipe(Meal.EGGPLANT_HAPPY, "[[Cafe]] on Tuesdays in [[Autumn]]");//Shop
        addFryingPanRecipe(Meal.EGG_SCRAMBLED, "10,000RP with [[Danieru]]");
        addFryingPanRecipe(Meal.OMELET, "[[Cafe]] on Mondays in [[Winter]]");//Shop
        addFryingPanRecipe(Meal.OMELET_RICE, "10,000RP with [[Brandon]]"); //Brandon 10000RP
        addFryingPanRecipe(Meal.TOAST_FRENCH, "10,000RP with [[Jade]]"); //Jade 10000RP
        addFryingPanRecipe(Meal.DOUGHNUT, "10,000RP with [[Tiberius]]"); //Tiberius 10000RP
        addFryingPanRecipe(Meal.FISH_GRILLED, "10,000RP with [[Jakob]]"); //Jacob 10000RP
        addFryingPanRecipe(Meal.PANCAKE, "[[Cafe]] on Fridays in [[Winter]]");//Shop
        addFryingPanRecipe(Meal.POTSTICKER, "10,000RP with [[Jenni]]"); //Jenni 10000RP
        addFryingPanRecipe(Meal.RISOTTO, "10,000RP with [[Cloe]]"); //Cloe 10000RP

        addMixerRecipe(Meal.JUICE_PINEAPPLE, "5000RP with [[Yulif]]"); //Yulif 5000RP
        addMixerRecipe(Meal.JUICE_TOMATO, "[[Cafe]] on Saturdays in [[Summer]]"); //Shop
        addMixerRecipe(Meal.MILK_STRAWBERRY, "10,000RP with [[Harvest Goddess]]"); //Goddess 10000RP Recipe
        addMixerRecipe(Meal.JUICE_VEGETABLE, "[[Cafe]] on Mondays in [[Spring]]");//Shop
        addMixerRecipe(Meal.LATTE_VEGETABLE, "5000RP with [[Tomas]]"); //Thomas 5000RP
        addMixerRecipe(Meal.KETCHUP, "[[Cafe]] on Mondays in [[Summer]]");
        addMixerRecipe(Meal.BUTTER, "5000RP with [[Danieru]]"); //Daniel 5000RP
        addMixerRecipe(Meal.FISHSTICKS, "5000RP with [[Jim]]");//Jim 5000RP

        addNoUtensilRecipe(Meal.TURNIP_PICKLED, "Completing [[Cafe Tutorial]]"); //Cafe Reward
        addNoUtensilRecipe(Meal.CUCUMBER_PICKLED, "[[Cafe]] on Fridays in [[Spring]]");//Shop
        addNoUtensilRecipe(Meal.SALAD, "5000RP with [[Jenni]]"); //Jenni 5000RP
        addNoUtensilRecipe(Meal.SANDWICH, "[[Cafe]] on Wednesdays in [[Autumn]]");//Shop
        addNoUtensilRecipe(Meal.SUSHI, "[[Cafe]] on Tuesdays in [[Spring]]");//Shop
        addNoUtensilRecipe(Meal.SASHIMI, "[[Cafe]] on Wednesdays in [[Spring]]");
        addNoUtensilRecipe(Meal.SASHIMI_CHIRASHI, "[[Cafe]] on Thursdays in [[Spring]]");//Shop
        addNoUtensilRecipe(Meal.ICE_CREAM, "10,000RP with [[Candice]]"); //Candice 10000RP

        addPotRecipe(Meal.MILK_HOT, "5000RP with [[Candice]]"); //Candice 5000RP Recipe
        addPotRecipe(Meal.CHOCOLATE_HOT, "5000RP with [[Liara]]"); //Liara 5000RP
        addPotRecipe(Meal.EGG_BOILED, "[[Cafe]] on Tuesdays in [[Winter]]");//Shop
        addPotRecipe(Meal.SPINACH_BOILED, "[[Cafe]] on Saturdays in [[Autumn]]");//Shop
        addPotRecipe(Meal.POTATO_CANDIED, "5000RP with [[Johan]]"); //Girafi 5000RP
        addPotRecipe(Meal.DUMPLINGS, "10,000RP with [[Tomas]]"); //Thomas 10000RP
        addPotRecipe(Meal.NOODLES, "5000RP with [[Cloe]]"); //Cloe 5000RP
        addPotRecipe(Meal.SOUP_RICE, "5000RP with [[Brandon]]"); //Brandon 5000RP
        addPotRecipe(Meal.PORRIDGE, "5000RP with [[Katlin]]"); //Katlin 5000RP
        addPotRecipe(Meal.EGG_OVERRICE, "[[Cafe]] on Wednesdays in [[Winter]]");//Shop
        addPotRecipe(Meal.STEW, "10,000RP with [[Katlin]]"); //Katlin 10000RP
        addPotRecipe(Meal.STEW_PUMPKIN, "[[Cafe]] on Tuesdays in [[Summer]]");//Shop
        addPotRecipe(Meal.STEW_FISH, "5000RP with [[Jakob]]"); //Jacob 5000RP
        addPotRecipe(Meal.JAM_STRAWBERRY, "5000RP with [[Harvest Goddess]]"); //Goddess 5000RP
        addPotRecipe(Meal.JAM_APPLE, "20,000RP with [[Jade]]"); //Jade 20000RP


        addOvenRecipe(Meal.CORN_BAKED, "5000RP with [[Ashlee]]"); //Ashlee 500RP
        addOvenRecipe(Meal.RICEBALLS_TOASTED, "[[Cafe]] on Sundays in [[Autumn]]");//Shop
        addOvenRecipe(Meal.TOAST, "5000RP with [[Jade]]"); //Jade 5000RP
        addOvenRecipe(Meal.DINNERROLL, "5000RP with [[Tiberius]]"); //Tiberius 5000RP
        addOvenRecipe(Meal.DORIA, "[[Cafe]] on Thursdays in [[Summer]]");//Shop
        addOvenRecipe(Meal.COOKIES, "7500 RP with [[Liara]]");
        addOvenRecipe(Meal.COOKIES_CHOCOLATE, "10,000RP with [[Liara]]"); //Liara 10000RP
        addOvenRecipe(Meal.CAKE_CHOCOLATE, "10,000RP with [[Yulif]]"); //Yulif RP 100000
        //Added in 0.6+
        addOvenRecipe(Meal.BUN_JAM, "25,000RP with [[Jade]]"); //Jade 25000RP
        addOvenRecipe(Meal.PIE_APPLE, "20,000RP with [[Katlin]]"); //Katlin 20000RP

        registerOverride("flour", "Flour is a cooking recipe made in the [[Mixer]]. You can also purchase flour from the [[General Store]] for {{gold|50}} which is much cheaper than turning your expensive [[Wheat]] in to flour.");
    }

    public static void save(StringBuilder builder) {
        try {
            PrintWriter writer = new PrintWriter("export.txt", "UTF-8");
            writer.write(builder.toString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static StringBuilder none() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        return builder;
    }
}
