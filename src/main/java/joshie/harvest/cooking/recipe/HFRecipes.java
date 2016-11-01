package joshie.harvest.cooking.recipe;

import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemIngredients;
import joshie.harvest.core.util.annotations.HFLoader;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static joshie.harvest.cooking.recipe.HFIngredients.*;
import static joshie.harvest.cooking.recipe.RecipeHelper.*;

@HFLoader
public class HFRecipes {
    public static final Recipe NULL_RECIPE = new Recipe(Utensil.COUNTER);

    public static void preInit() {
        //Recipes ; http://fogu.com/hm4/farm/stamina_chart.htm for numbers
        //Hunger = Stamina / 3.5F, Saturation = Stamina/17.5
        //Frying Pan
        addFryingPanRecipe("pancake_savoury", 1F, 1F, FLOUR, CABBAGE, OIL, EGG).setOptionalIngredients(ONION); //Jim 10000RP
        addFryingPanRecipe("fries_french", 1F, 1F, POTATO, OIL).setOptionalIngredients(SALT); //Girafi 10000RP
        addFryingPanRecipe("popcorn", 1F, 1F, CORN).setOptionalIngredients(BUTTER, SALT); //Ashlee 10000RP
        addFryingPanRecipe("cornflakes", 1F, 1F, CORN, MILK).setOptionalIngredients(SUGAR); //Shop
        addFryingPanRecipe("eggplant_happy", 1F, 1F, EGGPLANT).setOptionalIngredients(SUGAR);//Shop
        addFryingPanRecipe("egg_scrambled", 1F, 1F, EGG, OIL).setOptionalIngredients(BUTTER, MAYONNAISE, SALT);//TODO: Alt >>>.setAlternativeTexture(INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.EGG_SCRAMBLED)); //Daniel 10000RP
        addFryingPanRecipe("omelet", 1F, 1F, EGG, OIL, MILK).setOptionalIngredients(SALT);//Shop
        addFryingPanRecipe("omelet_rice", 1F, 1F, EGG, MILK, OIL, RICEBALL).setOptionalIngredients(CABBAGE, ONION, MUSHROOM, GREEN_PEPPER, SALT); //Brandon 10000RP
        addFryingPanRecipe("toast_french", 1F, 1F, EGG, BREAD, OIL, SUGAR).setOptionalIngredients(BUTTER); //Jade 10000RP
        addFryingPanRecipe("doughnut", 1F, 1F, EGG, MILK, BUTTER, FLOUR, OIL); //Tiberius 10000RP
        addFryingPanRecipe("fish_grilled", 1F, 1F, FISH, OIL, SALT); //Jacob 10000RP
        addFryingPanRecipe("pancake", 1F, 1F, EGG, MILK, FLOUR, OIL).setOptionalIngredients(SUGAR, BUTTER);//Shop
        addFryingPanRecipe("potsticker", 1F, 1F, CABBAGE, ONION, FLOUR, OIL); //Jenni 10000RP
        addFryingPanRecipe("risotto", 1F, 1F, TOMATO, ONION, RICEBALL, OIL); //Cloe 10000RP
        //Added in 0.6+
        addFryingPanRecipe("stir_fry", 1F, 1F, CABBAGE, OIL).setOptionalIngredients(ONION, CABBAGE, BAMBOO, MATSUTAKE, EGGPLANT, GREEN_PEPPER);
        addFryingPanRecipe("rice_fried", 1F, 1F, RICEBALL, OIL, EGG).setOptionalIngredients(CARROT, ONION, BAMBOO, GREEN_PEPPER, FISH);
        addFryingPanRecipe("souffle_apple", 1F, 1F, APPLE);
        addFryingPanRecipe("bread_curry", 1F, 1F, BREAD, CURRY_POWDER, OIL);
        addFryingPanRecipe("noodles_thick_fried", 1F, 1F, NOODLES, OIL).setOptionalIngredients(CABBAGE, ONION, FISH, BAMBOO, CARROT, EGGPLANT);
        addFryingPanRecipe("tempura", 1F, 1F, EGG, FLOUR, OIL).setOptionalIngredients(EGGPLANT, GREEN_PEPPER, CARROT, CABBAGE, ONION);
        addFryingPanRecipe("curry_dry", 1F, 1F, RICEBALL, CURRY_POWDER).setOptionalIngredients(ONION, GREEN_PEPPER, FISH, POTATO, EGGPLANT, CARROT);

        //Mixer
        addMixerRecipe("juice_pineapple", 1F, 1F, PINEAPPLE).setOptionalIngredients(SALT, SUGAR); //Yulif 5000RP
        addMixerRecipe("juice_tomato", 1F, 1F, TOMATO).setOptionalIngredients(SALT); //Shop
        addMixerRecipe("milk_strawberry", 1F, 1F, STRAWBERRY, MILK).setOptionalIngredients(SUGAR); //Candice 10000RP Recipe
        addMixerRecipe("juice_vegetable", 1F, 1F, VEGETABLE_JUICE_BASE).setOptionalIngredients(CUCUMBER, ONION, CABBAGE, TOMATO, SPINACH, CARROT, GREEN_PEPPER, TURNIP, SALT);//Shop
        addMixerRecipe("latte_vegetable", 1F, 1F, VEGETABLE_JUICE_BASE, MILK).setOptionalIngredients(CUCUMBER, ONION, CABBAGE, TOMATO, SPINACH, CARROT, GREEN_PEPPER, TURNIP, SALT); //Thomas 5000RP
        addMixerRecipe("ketchup", 0.25F, 0.1F, TOMATO, ONION).setOptionalIngredients(SALT, SUGAR);//TODO: ALT >>>.setAlternativeTexture(INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.KETCHUP));//Shop
        addMixerRecipe("butter", false, 1F, 0.5F, MILK).setOptionalIngredients(SALT);//TODO:.setAlternativeTexture(INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.BUTTER)); //Daniel 5000RP
        addMixerRecipe("fishsticks", false, 1F, 1F, FISH).setOptionalIngredients(SALT);//Jim 5000RP
        //Added in 0.6+
        addMixerRecipe("juice_grape", 1.5F, 1.2F, GRAPE);
        addMixerRecipe("juice_peach", 1.5F, 1.2F, PEACH);
        addMixerRecipe("juice_banana", 1.5F, 1.2F, BANANA);
        addMixerRecipe("juice_orange", 1.5F, 1.2F, ORANGE);
        addMixerRecipe("juice_apple", 1.5F, 1.2F, APPLE);
        addMixerRecipe("juice_fruit", 0.5F, 0.5F, FRUIT_JUICE_BASE).setOptionalIngredients(FRUITS).setMaximumOptionalIngredients(5);
        addMixerRecipe("latte_fruit", 0.4F, 0.6F, FRUIT_JUICE_BASE, MILK).setOptionalIngredients(FRUITS).setMaximumOptionalIngredients(5);
        addMixerRecipe("juice_mix", 0.5F, 0.5F, FRUIT_JUICE_BASE, VEGETABLE_JUICE_BASE).setOptionalIngredients(FRUITS, VEGETABLE_JUICE_BASE, TURNIP, SALT).setMaximumOptionalIngredients(7);
        addMixerRecipe("latte_mix", 0.4F, 0.6F, FRUIT_JUICE_BASE, VEGETABLE_JUICE_BASE, MILK).setOptionalIngredients(FRUITS, VEGETABLE_JUICE_BASE, SALT).setMaximumOptionalIngredients(7);

        //Vanilla Mix Recipes
        addMixerRecipe("beetroot_soup", new ItemStack(Items.BEETROOT_SOUP), BEETROOT, TOMATO, ONION, OIL);
        addMixerRecipe("flour", HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.FLOUR), WHEAT);

        //Hand
        addNoUtensilRecipe("turnip_pickled", 1F, 1F, TURNIP).setOptionalIngredients(SALT); //Cafe Reward
        addNoUtensilRecipe("cucumber_pickled", 1F, 1F, CUCUMBER).setOptionalIngredients(SALT);//Shop
        addNoUtensilRecipe("salad", 1F, 1F, SALAD_BASE).setOptionalIngredients(MUSHROOM, CUCUMBER, CABBAGE, TOMATO, CARROT, SALT); //Jenni 5000RP
        addNoUtensilRecipe("sandwich", 1F, 1F, BREAD, SANDWICH_BASE).setOptionalIngredients(BUTTER, TOMATO, CUCUMBER, SALT, MAYONNAISE, MUSHROOM);//Shop
        addNoUtensilRecipe("sushi", 1F, 1F, SASHIMI, RICEBALL);//Shop
        addNoUtensilRecipe("sashimi", 1F, 1F, FISH);//TODO:.setAlternativeTexture(INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.SASHIMI));//Shop
        addNoUtensilRecipe("sashimi_chirashi", 1F, 1F, SASHIMI, SCRAMBLED_EGG, RICEBALL, SASHIMI_VEGETABLE);//Shop
        //Added in 0.6+
        addNoUtensilRecipe("sandwich_fruit", 0.75F, 1F, BREAD, FRUITS).setOptionalIngredients(FRUITS).setMaximumOptionalIngredients(5);
        addNoUtensilRecipe("rice_bamboo", 1F, 1F, BAMBOO, RICEBALL);
        addNoUtensilRecipe("rice_matsutake", 1F, 1F, MATSUTAKE, RICEBALL);
        addNoUtensilRecipe("rice_mushroom", 1F, 1F, BROWN_MUSHROOM, RICEBALL);
        addNoUtensilRecipe("bread_raisin", 1F, 1F, BREAD, GRAPE);
        addNoUtensilRecipe("ice_cream", 1F, 1F, MILK, EGG).setOptionalIngredients(PINEAPPLE, ORANGE, STRAWBERRY, GRAPE, PEACH, BANANA);

        //Pot
        addPotRecipe("milk_hot", true, 1F, 1F, MILK).setOptionalIngredients(SUGAR); //Candice 5000RP Recipe
        addPotRecipe("chocolate_hot", true, 1F, 1F, MILK, CHOCOLATE).setOptionalIngredients(SUGAR); //Liara 5000RP
        addPotRecipe("egg_boiled", 1F, 1F, EGG).setOptionalIngredients(SALT);//Shop
        addPotRecipe("spinach_boiled", 1F, 1F, SPINACH);//Shop
        addPotRecipe("potato_candied", 1F, 1F, SWEET_POTATO).setOptionalIngredients(SUGAR); //Girafi 5000RP
        addPotRecipe("dumplings", true, 1F, 1F, CABBAGE, ONION, FLOUR, OIL).setOptionalIngredients(SUGAR); //Thomas 10000RP
        addPotRecipe("noodles", 1F, 1F, FLOUR).setOptionalIngredients(SALT); //Cloe 5000RP
        addPotRecipe("soup_rice", 3F, 1F, RICEBALL); //Brandon 5000RP
        addPotRecipe("porridge", 1F, 1F, MILK, RICEBALL).setOptionalIngredients(SUGAR); //Katlin 5000RP
        addPotRecipe("egg_overrice", 1F, 1F, EGG, RICEBALL).setOptionalIngredients(SALT);//Shop
        addPotRecipe("stew", 1F, 1F, MILK, FLOUR).setOptionalIngredients(EGGPLANT, ONION, POTATO, CARROT, GREEN_PEPPER, FISH, SALT); //Katlin 10000RP
        addPotRecipe("stew_pumpkin", true, 1F, 1F, PUMPKIN).setOptionalIngredients(SUGAR, SALT);//Shop
        addPotRecipe("stew_fish", 1F, 1F, FISH).setOptionalIngredients(SALT); //Jacob 5000RP
        //Added in 0.6+
        addPotRecipe("jam_strawberry", 1F, 1F, STRAWBERRY).setOptionalIngredients(WINE);
        addPotRecipe("jam_apple", 1F, 1F, APPLE).setOptionalIngredients(WINE);
        addPotRecipe("jam_grape", 1F, 1F, GRAPE).setOptionalIngredients(WINE);
        addPotRecipe("marmalade", 1F, 1F, ORANGE).setOptionalIngredients(WINE);
        addPotRecipe("noodles_tempura", 1F, 1F, TEMPURA, NOODLES);
        addPotRecipe("rice_tempura", 1F, 1F, TEMPURA, RICEBALL);

        //Vanilla Pot Recipes
        addPotRecipe("rabbit_stew", new ItemStack(Items.RABBIT_STEW), BAKED_POTATO, CARROT, RABBIT_COOKED, MUSHROOM);
        addPotRecipe("brown_mushroom", new ItemStack(Items.MUSHROOM_STEW), RED_MUSHROOM, BROWN_MUSHROOM);

        //Oven
        addOvenRecipe("corn_baked", 1F, 1F, CORN).setOptionalIngredients(OIL, BUTTER, SALT); //Ashlee 500RP
        addOvenRecipe("riceballs_toasted", 1F, 1F, RICEBALL).setOptionalIngredients(SUGAR, SALT);//Shop
        addOvenRecipe("toast", 1F, 1F, BREAD).setOptionalIngredients(BUTTER); //Jade 5000RP
        addOvenRecipe("dinnerroll", 1F, 1F, EGG, MILK, BUTTER); //Tiberius 5000RP
        addOvenRecipe("doria", 1F, 1F, ONION, BUTTER, MILK, RICEBALL, FLOUR);//Shop
        addOvenRecipe("cookies", 1F, 0.4F, EGG, FLOUR, BUTTER).setOptionalIngredients(SUGAR);//TODO.setAlternativeTexture(INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.COOKIES)); //Liara 7500RP
        addOvenRecipe("cookies_chocolate", 1F, 0.5F, COOKIES, CHOCOLATE); //Liara 10000RP
        addOvenRecipe("cake_chocolate", 1F, 1F, EGG, FLOUR, BUTTER, CHOCOLATE).setOptionalIngredients(SUGAR, FRUITS); //Yulif RP 100000
        //Added in 0.6+
        addOvenRecipe("jam_bun", 1F, 1F, MILK, EGG, JAM);
        addOvenRecipe("sweet_potatoes", 1F, 1F, EGG, BUTTER, SWEET_POTATO);
        addOvenRecipe("cake", 1F, 1F, EGG, FLOUR, BUTTER, CAKE_FRUIT).setOptionalIngredients(ORANGE, PINEAPPLE, STRAWBERRY, PEACH, GRAPE);
        addOvenRecipe("pie_apple", 1F, 1F, APPLE, EGG, BUTTER, FLOUR);

        //Vanilla Oven Recipes
        addOvenRecipe("bread", new ItemStack(Items.BREAD), FLOUR);
        addOvenRecipe("baked_potato", new ItemStack(Items.BAKED_POTATO), POTATO);
        addOvenRecipe("cooked_chicken", new ItemStack(Items.COOKED_CHICKEN), CHICKEN);
        addOvenRecipe("cooked_beef", new ItemStack(Items.COOKED_BEEF), BEEF);
        addOvenRecipe("cooked_pork", new ItemStack(Items.COOKED_PORKCHOP), PORK);
        addOvenRecipe("cooked_mutton", new ItemStack(Items.COOKED_MUTTON), MUTTON);
        addOvenRecipe("cooked_rabbit", new ItemStack(Items.COOKED_RABBIT), RABBIT);
        addOvenRecipe("cooked_cod", new ItemStack(Items.COOKED_FISH, 1, 0), COD);
        addOvenRecipe("cooked_salmon", new ItemStack(Items.COOKED_FISH, 1, 1), SALMON);
        addOvenRecipe("pumpkin_pie", new ItemStack(Items.PUMPKIN_PIE), PUMPKIN, SUGAR, EGG);
    }
}
