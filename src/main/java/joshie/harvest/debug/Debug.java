package joshie.harvest.debug;

import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.core.util.annotations.HFLoader;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import static joshie.harvest.core.lib.HFModInfo.MODID;

@HFLoader
public class Debug {
    private static void register(String name, String description) {
        Recipe recipe = Recipe.REGISTRY.getValue(new ResourceLocation(MODID, name));
        CommandExportRecipe.DESCRIPTIONS.put(recipe, description);
    }

    private static void register(Meal meal, String description) {
        Recipe recipe = Recipe.REGISTRY.getValue(new ResourceLocation(MODID, meal.name().toLowerCase()));
        CommandExportRecipe.DESCRIPTIONS.put(recipe, description);
    }

    private static void registerOverride(String name, String description) {
        Recipe recipe = Recipe.REGISTRY.getValue(new ResourceLocation(MODID, name));
        CommandExportRecipe.INFO_OVERRIDE.put(recipe, description);
    }

    public static void postInit() {
        register(Meal.BREAD_CURRY, "20,000RP with [[Jim]]"); //Jim 20000RP
        register(Meal.PANCAKE_SAVOURY, "10,000RP with [[Jim]]"); //Jim 10000RP
        register(Meal.FRIES_FRENCH, "10,000RP with [[Johan]]"); //Girafi 10000RP
        register(Meal.POPCORN, "10,000RP with [[Ashlee]]"); //Ashlee 10000RP
        register(Meal.CORNFLAKES, "[[Cafe]] on Sundays in [[Summer]]"); //Shop
        register(Meal.EGGPLANT_HAPPY, "[[Cafe]] on Tuesdays in [[Autumn]]");//Shop
        register(Meal.EGG_SCRAMBLED, "10,000RP with [[Danieru]]");
        register(Meal.OMELET, "[[Cafe]] on Mondays in [[Winter]]");//Shop
        register(Meal.OMELET_RICE, "10,000RP with [[Brandon]]"); //Brandon 10000RP
        register(Meal.TOAST_FRENCH, "10,000RP with [[Jade]]"); //Jade 10000RP
        register(Meal.DOUGHNUT, "10,000RP with [[Tiberius]]"); //Tiberius 10000RP
        register(Meal.FISH_GRILLED, "10,000RP with [[Jakob]]"); //Jacob 10000RP
        register(Meal.PANCAKE, "[[Cafe]] on Fridays in [[Winter]]");//Shop
        register(Meal.POTSTICKER, "10,000RP with [[Jenni]]"); //Jenni 10000RP
        register(Meal.RISOTTO, "10,000RP with [[Cloe]]"); //Cloe 10000RP
        register(Meal.CURRY_DRY, "20,000RP with [[Ashlee]]"); //Ashlee 20000RP
        register(Meal.SOUFFLE_APPLE, "20,000RP with [[Yulif]]"); //Yulif 20000RP
        register(Meal.BREAD_RAISIN, "20,000RP with [[Tiberius]]"); //Tiberius 20000RP

        register(Meal.JUICE_PINEAPPLE, "5000RP with [[Yulif]]"); //Yulif 5000RP
        register(Meal.JUICE_TOMATO, "[[Cafe]] on Saturdays in [[Summer]]"); //Shop
        register(Meal.JUICE_FRUIT, "[[Cafe]] on Fridays in [[Summer]]"); //Shop
        register(Meal.MILK_STRAWBERRY, "10,000RP with [[Harvest Goddess]]"); //Goddess 10000RP Recipe
        register(Meal.JUICE_VEGETABLE, "[[Cafe]] on Mondays in [[Spring]]");//Shop
        register(Meal.LATTE_VEGETABLE, "5000RP with [[Tomas]]"); //Thomas 5000RP
        register(Meal.LATTE_FRUIT, "[[Cafe]] on Fridays in [[Autumn]]"); //Shop
        register(Meal.KETCHUP, "[[Cafe]] on Mondays in [[Summer]]");
        register(Meal.BUTTER, "5000RP with [[Danieru]]"); //Daniel 5000RP
        register(Meal.FISHSTICKS, "5000RP with [[Jim]]");//Jim 5000RP
        register(Meal.JUICE_GRAPE, "[[Cafe]] once you have shipped a [[Grape]]"); //Ship grapes
        register(Meal.JUICE_PEACH, "[[Cafe]] once you have shipped a [[Peach]]"); //Ship a peach
        register(Meal.JUICE_BANANA, "[[Cafe]] once you have shipped a [[Banana]]"); //Ship a banana
        register(Meal.JUICE_ORANGE, "[[Cafe]] once you have shipped an [[Orange]]"); //Ship an orange
        register(Meal.JUICE_APPLE, "[[Cafe]] once you have shipped an [[Apple]]"); //Ship an apple
        register(Meal.LATTE_MIX, "20,000RP with [[Candice]]");
        register(Meal.JUICE_MIX, "[[Cafe]] on Thursdays in [[Winter]]");

        register(Meal.TURNIP_PICKLED, "Completing [[Cafe Tutorial]]"); //Cafe Reward
        register(Meal.CUCUMBER_PICKLED, "[[Cafe]] on Fridays in [[Spring]]");//Shop
        register(Meal.SALAD, "5000RP with [[Jenni]]"); //Jenni 5000RP
        register(Meal.SANDWICH, "[[Cafe]] on Wednesdays in [[Autumn]]");//Shop
        register(Meal.SANDWICH_FRUIT, "[[Cafe]] on Thursdays in [[Autumn]]");//Shop
        register(Meal.SUSHI, "[[Cafe]] on Tuesdays in [[Spring]]");//Shop
        register(Meal.SASHIMI, "[[Cafe]] on Wednesdays in [[Spring]]");
        register(Meal.SASHIMI_CHIRASHI, "[[Cafe]] on Thursdays in [[Spring]]");//Shop
        register(Meal.ICE_CREAM, "10,000RP with [[Candice]]"); //Candice 10000RP
        register("mayonnaise", "20,000RP with [[Danieru]]"); //Danieru 20000RP
        register(Meal.MILK_HOT, "5000RP with [[Candice]]"); //Candice 5000RP Recipe
        register(Meal.CHOCOLATE_HOT, "5000RP with [[Liara]]"); //Liara 5000RP
        register(Meal.EGG_BOILED, "[[Cafe]] on Tuesdays in [[Winter]]");//Shop
        register(Meal.SPINACH_BOILED, "[[Cafe]] on Saturdays in [[Autumn]]");//Shop
        register(Meal.POTATO_CANDIED, "5000RP with [[Johan]]"); //Girafi 5000RP
        register(Meal.DUMPLINGS, "10,000RP with [[Tomas]]"); //Thomas 10000RP
        register(Meal.NOODLES, "5000RP with [[Cloe]]"); //Cloe 5000RP
        register(Meal.SOUP_RICE, "5000RP with [[Brandon]]"); //Brandon 5000RP
        register(Meal.PORRIDGE, "5000RP with [[Katlin]]"); //Katlin 5000RP
        register(Meal.EGG_OVERRICE, "[[Cafe]] on Wednesdays in [[Winter]]");//Shop
        register(Meal.STEW, "10,000RP with [[Katlin]]"); //Katlin 10000RP
        register(Meal.STEW_PUMPKIN, "[[Cafe]] on Tuesdays in [[Summer]]");//Shop
        register(Meal.STEW_FISH, "5000RP with [[Jakob]]"); //Jacob 5000RP
        register(Meal.JAM_STRAWBERRY, "5000RP with [[Harvest Goddess]]"); //Goddess 5000RP
        register(Meal.JAM_APPLE, "20,000RP with [[Jade]]"); //Jade 20000RP
        register(Meal.JAM_GRAPE, "20,000RP with [[Jade]]"); //Jade 20000RP
        register(Meal.MARMALADE, "20,000RP with [[Jade]]"); //Jade 20000RP
        register(Meal.RICE_MATSUTAKE, "[[Cafe]] on Saturdays in [[Winter]]");
        register(Meal.RICE_MUSHROOM, "[[Cafe]] on Sundays in [[Winter]]");
        register(Meal.RICE_FRIED, "[[Cafe]] on Wednesdays in [[Summer]]");
        register("rabbit_stew", "N/A");
        register("brown_mushroom", "N/A");

        register(Meal.SWEET_POTATOES, "[[Cafe]] on Mondays in [[Autumn]]");
        register(Meal.CORN_BAKED, "5000RP with [[Ashlee]]"); //Ashlee 500RP
        register(Meal.RICEBALLS_TOASTED, "[[Cafe]] on Sundays in [[Autumn]]");//Shop
        register(Meal.TOAST, "5000RP with [[Jade]]"); //Jade 5000RP
        register(Meal.DINNERROLL, "5000RP with [[Tiberius]]"); //Tiberius 5000RP
        register(Meal.DORIA, "[[Cafe]] on Thursdays in [[Summer]]");//Shop
        register(Meal.COOKIES, "7500 RP with [[Liara]]");
        register(Meal.COOKIES_CHOCOLATE, "10,000RP with [[Liara]]"); //Liara 10000RP
        register(Meal.CAKE_CHOCOLATE, "10,000RP with [[Yulif]]"); //Yulif RP 100000
        //Added in 0.6+
        register(Meal.BUN_JAM, "25,000RP with [[Jade]]"); //Jade 25000RP
        register(Meal.PIE_APPLE, "20,000RP with [[Katlin]]"); //Katlin 20000RP
        register("bread", "N/A");
        register("baked_potato", "N/A");
        register("cooked_chicken", "N/A");
        register("cooked_beef", "N/A");
        register("cooked_pork", "N/A");
        register("cooked_mutton", "N/A");
        register("cooked_rabbit", "N/A");
        register("cooked_cod", "N/A");
        register("cooked_salmon", "N/A");
        register("pumpkin_pie", "N/A");
        register("beetroot_soup", "N/A");
        register("flour", "N/A");

        register(Meal.TEMPURA, "17,500RP with [[Liara]]"); //Liara 17500RP
        register(Meal.RICE_TEMPURA, "20,000RP with [[Liara]]"); //Liara 20000RP
        register(Meal.NOODLES_TEMPURA, "22,500RP with [[Liara]]"); //Liara 22500RP
        register(Meal.STIR_FRY, "20,000RP with [[Jenni]]"); //Jenni 20000RP
        register(Meal.RICE_BAMBOO, "20,000RP with [[Brandon]]"); //Brandon 20000RP
        register(Meal.NOODLES_THICK_FRIED, "20,000RP with [[Cloe]]"); //Cloe 20000RP
        register(Meal.LATTE_FRUIT, "20,000RP with [[Tomas]]"); //Tomas 20000RP

        registerOverride("flour", "Flour is a cooking recipe made in the [[Mixer]]. You can also purchase flour from the [[General Store]] for {{gold|50}} which is much cheaper than turning your expensive [[Wheat]] in to flour.");
    }

    public static void save(@Nonnull StringBuilder builder) {
        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("export.rtf"), "UTF-16"));
            out.write(TextFormatting.getTextWithoutFormattingCodes(builder.toString()));
            out.close();
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
