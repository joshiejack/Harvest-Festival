package joshie.harvest.cooking.recipe;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.api.core.Size;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemIngredients;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.core.util.annotations.HFLoader;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static joshie.harvest.cooking.recipe.HFIngredients.*;
import static joshie.harvest.cooking.recipe.RecipeHelper.*;

@HFLoader
public class HFRecipes {
    public static final Recipe NULL_RECIPE = new Recipe(Utensil.COUNTER);

    public static void init() {
        addFryingPanRecipes();
        addMixerRecipes();
        addNoUtensilRecipes();
        addPotRecipes();
        addOvenRecipes();
    }

       private static void addFryingPanRecipes() {
        //Added in 0.5+
        addFryingPanRecipe(Meal.PANCAKE_SAVOURY, 2.5F, 1.1F, FLOUR, CABBAGE, OIL, EGG).setOptionalIngredients(ONION); //Jim 10000RP
        addFryingPanRecipe(Meal.FRIES_FRENCH, 4F, 1.2F, POTATO, OIL).setOptionalIngredients(SALT); //Girafi 10000RP
        addFryingPanRecipe(Meal.POPCORN, 3F, 1F, CORN).setOptionalIngredients(BUTTER, SALT); //Ashlee 10000RP
        addFryingPanRecipe(Meal.CORNFLAKES, 1.1F, 0.8F, CORN, MILK).setOptionalIngredients(SUGAR); //Shop
        addFryingPanRecipe(Meal.EGGPLANT_HAPPY, 1.5F, 1.2F, EGGPLANT).setOptionalIngredients(SUGAR);//Shop
        addFryingPanRecipe(Meal.EGG_SCRAMBLED, 1.5F, 1.5F, EGG, OIL).setOptionalIngredients(BUTTER, MAYONNAISE, SALT);//TODO: Alt >>>.setAlternativeTexture(INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.EGG_SCRAMBLED)); //Daniel 10000RP
        addFryingPanRecipe(Meal.OMELET, 1.2F, 1.1F, EGG, OIL, MILK).setOptionalIngredients(SALT);//Shop
        addFryingPanRecipe(Meal.OMELET_RICE, 1.15F, 1.1F, EGG, MILK, OIL, RICEBALL).setOptionalIngredients(CABBAGE, ONION, MUSHROOM, GREEN_PEPPER, SALT); //Brandon 10000RP
        addFryingPanRecipe(Meal.TOAST_FRENCH, 1.2F, 1.2F, EGG, BREAD, OIL, SUGAR).setOptionalIngredients(BUTTER); //Jade 10000RP
        addFryingPanRecipe(Meal.DOUGHNUT, 1.2F, 0.8F, EGG, MILK, BUTTER, FLOUR, OIL); //Tiberius 10000RP
        addFryingPanRecipe(Meal.FISH_GRILLED, 1.1F, 1.1F, FISH, OIL, SALT); //Jacob 10000RP
        addFryingPanRecipe(Meal.PANCAKE, 1.2F, 1.2F, EGG, MILK, FLOUR, OIL).setOptionalIngredients(SUGAR);//Shop
        addFryingPanRecipe(Meal.POTSTICKER, 1.5F, 2F, CABBAGE, ONION, FLOUR, OIL); //Jenni 10000RP
        addFryingPanRecipe(Meal.RISOTTO, 1.5F, 1.5F, TOMATO, ONION, RICEBALL, OIL); //Cloe 10000RP
        //Added in 0.6+
        addFryingPanRecipe(Meal.STIR_FRY, 1.2F, 1F, CABBAGE, OIL).setOptionalIngredients(ONION, CABBAGE, BAMBOO, MATSUTAKE, EGGPLANT, GREEN_PEPPER);
        addFryingPanRecipe(Meal.RICE_FRIED, 1.5F, 1F, RICEBALL, OIL, EGG).setOptionalIngredients(CARROT, ONION, BAMBOO, GREEN_PEPPER, FISH);
        addFryingPanRecipe(Meal.SOUFFLE_APPLE, 2F, 1F, APPLE);
        addFryingPanRecipe(Meal.BREAD_CURRY, 1.1F, 1F, BREAD, CURRY_POWDER, OIL);
        addFryingPanRecipe(Meal.NOODLES_THICK_FRIED, 1.2F, 1F, NOODLES, OIL).setOptionalIngredients(CABBAGE, ONION, FISH, BAMBOO, CARROT, EGGPLANT);
        addFryingPanRecipe(Meal.TEMPURA, 1.2F, 1F, EGG, FLOUR, OIL).setOptionalIngredients(EGGPLANT, GREEN_PEPPER, CARROT, CABBAGE, ONION);
        addFryingPanRecipe(Meal.CURRY_DRY, 1.2F, 1F, RICEBALL, CURRY_POWDER).setOptionalIngredients(ONION, GREEN_PEPPER, FISH, POTATO, EGGPLANT, CARROT);
    }

    private static void addMixerRecipes() {
        //Added in 0.5+
        addMixerRecipe(Meal.JUICE_PINEAPPLE, 1.5F, 0.5F, PINEAPPLE).setOptionalIngredients(SALT, SUGAR); //Yulif 5000RP
        addMixerRecipe(Meal.JUICE_TOMATO, 1.5F, 0.5F, TOMATO).setOptionalIngredients(SALT); //Shop
        addMixerRecipe(Meal.MILK_STRAWBERRY, 1.5F, 0.8F, STRAWBERRY, MILK).setOptionalIngredients(SUGAR); //Goddess 10000RP Recipe
        addMixerRecipe(Meal.JUICE_VEGETABLE, 1.5F, 0.6F, VEGETABLE_JUICE_BASE).setOptionalIngredients(CUCUMBER, ONION, CABBAGE, TOMATO, SPINACH, CARROT, GREEN_PEPPER, TURNIP, SALT);//Shop
        addMixerRecipe(Meal.LATTE_VEGETABLE, 1.5F, 0.5F, VEGETABLE_JUICE_BASE, MILK).setOptionalIngredients(CUCUMBER, ONION, CABBAGE, TOMATO, SPINACH, CARROT, GREEN_PEPPER, TURNIP, SALT); //Thomas 5000RP
        addMixerRecipe(Meal.KETCHUP, 0.25F, 0.1F, TOMATO, ONION).setOptionalIngredients(SALT, SUGAR); //Shop
        addMixerRecipe(Meal.BUTTER, false, 0.9F, 1.5F, MILK).setOptionalIngredients(SALT); //Daniel 5000RP
        addMixerRecipe(Meal.FISHSTICKS, false, 2F, 1.1F, FISH).setOptionalIngredients(SALT);//Jim 5000RP
        //Added in 0.6+
        addMixerRecipe(Meal.JUICE_GRAPE, 1.5F, 1.2F, GRAPE);
        addMixerRecipe(Meal.JUICE_PEACH, 1.5F, 1.2F, PEACH);
        addMixerRecipe(Meal.JUICE_BANANA, 1.5F, 1.2F, BANANA);
        addMixerRecipe(Meal.JUICE_ORANGE, 1.5F, 1.2F, ORANGE);
        addMixerRecipe(Meal.JUICE_APPLE, 1.5F, 1.2F, APPLE);
        addMixerRecipe(Meal.JUICE_FRUIT, 1.05F, 0.5F, FRUIT_JUICE_BASE).setOptionalIngredients(FRUITS).setMaximumOptionalIngredients(5);
        addMixerRecipe(Meal.LATTE_FRUIT, 1.04F, 0.6F, FRUIT_JUICE_BASE, MILK).setOptionalIngredients(FRUITS).setMaximumOptionalIngredients(5);
        addMixerRecipe(Meal.JUICE_MIX, 0.5F, 0.5F, FRUIT_JUICE_BASE, VEGETABLE_JUICE_BASE).setOptionalIngredients(FRUITS, VEGETABLE_JUICE_BASE, TURNIP, SALT).setMaximumOptionalIngredients(7);
        addMixerRecipe(Meal.LATTE_MIX, 1.05F, 0.6F, FRUIT_JUICE_BASE, VEGETABLE_JUICE_BASE, MILK).setOptionalIngredients(FRUITS, VEGETABLE_JUICE_BASE, SALT).setMaximumOptionalIngredients(7);
        //Vanilla style
        addMixerRecipe("beetroot_soup", new ItemStack(Items.BEETROOT_SOUP), BEETROOT, TOMATO, ONION, OIL);
        addMixerRecipe("flour", HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.FLOUR), WHEAT);
    }

    private static void addNoUtensilRecipes() {
        //Added in 0.5+
        addNoUtensilRecipe(Meal.TURNIP_PICKLED, 2F, 1.5F, TURNIP).setOptionalIngredients(SALT); //Cafe Reward
        addNoUtensilRecipe(Meal.CUCUMBER_PICKLED, 2F, 1.5F, CUCUMBER).setOptionalIngredients(SALT);//Shop
        addNoUtensilRecipe(Meal.SALAD, 1.1F, 1.2F, SALAD_BASE).setOptionalIngredients(MUSHROOM, CUCUMBER, CABBAGE, TOMATO, CARROT, SALT); //Jenni 5000RP
        addNoUtensilRecipe(Meal.SANDWICH, 1.2F, 1.1F, BREAD, SANDWICH_BASE).setOptionalIngredients(BUTTER, TOMATO, CUCUMBER, SALT, MAYONNAISE, MUSHROOM);//Shop
        addNoUtensilRecipe(Meal.SUSHI, 1.2F, 1.2F, SASHIMI, RICEBALL);//Shop
        addNoUtensilRecipe(Meal.SASHIMI, 2.5F, 2F, FISH); //Shop
        addNoUtensilRecipe(Meal.SASHIMI_CHIRASHI, 1.1F, 0.9F, SASHIMI, SCRAMBLED_EGG, RICEBALL, SASHIMI_VEGETABLE);//Shop
        //Added in 0.6+
        addNoUtensilRecipe("mayonnaise", HFAnimals.ANIMAL_PRODUCT.getStack(Sizeable.MAYONNAISE, Size.MEDIUM), EGG);
        addNoUtensilRecipe(Meal.SANDWICH_FRUIT, 1.05F, 1F, BREAD, FRUITS).setOptionalIngredients(FRUITS).setMaximumOptionalIngredients(5);
        addNoUtensilRecipe(Meal.RICE_BAMBOO, 2F, 1.5F, BAMBOO, RICEBALL);
        addNoUtensilRecipe(Meal.RICE_MATSUTAKE, 2F, 1.5F, MATSUTAKE, RICEBALL);
        addNoUtensilRecipe(Meal.RICE_MUSHROOM, 2F, 1.5F, BROWN_MUSHROOM, RICEBALL);
        addNoUtensilRecipe(Meal.BREAD_RAISIN, 1.2F, 1.2F, BREAD, GRAPE);
        addNoUtensilRecipe(Meal.ICE_CREAM, 1.8F, 0.8F, MILK, EGG).setOptionalIngredients(PINEAPPLE, ORANGE, STRAWBERRY, GRAPE, PEACH, BANANA); //Candice 10000RP
    }

    private static void addPotRecipes() {
        //Added in 0.5+
        addPotRecipe(Meal.MILK_HOT, true, 1.5F, 1.2F, MILK).setOptionalIngredients(SUGAR); //Candice 5000RP Recipe
        addPotRecipe(Meal.CHOCOLATE_HOT, true, 1.4F, 1.4F, MILK, CHOCOLATE).setOptionalIngredients(SUGAR); //Liara 5000RP
        addPotRecipe(Meal.EGG_BOILED, 1.5F, 1.4F, EGG).setOptionalIngredients(SALT);//Shop
        addPotRecipe(Meal.SPINACH_BOILED, 2F, 1.2F, SPINACH);//Shop
        addPotRecipe(Meal.POTATO_CANDIED, 3F, 2F, SWEET_POTATO).setOptionalIngredients(SUGAR); //Girafi 5000RP
        addPotRecipe(Meal.DUMPLINGS, 1.1F, 1.1F, CABBAGE, ONION, FLOUR, OIL).setOptionalIngredients(SUGAR); //Thomas 10000RP
        addPotRecipe(Meal.NOODLES, 2F, 1.5F, FLOUR).setOptionalIngredients(SALT); //Cloe 5000RP
        addPotRecipe(Meal.SOUP_RICE, 3F, 1.2F, RICEBALL); //Brandon 5000RP
        addPotRecipe(Meal.PORRIDGE, 1.5F, 1.2F, MILK, RICEBALL).setOptionalIngredients(SUGAR); //Katlin 5000RP
        addPotRecipe(Meal.EGG_OVERRICE, 1.5F, 1.2F, EGG, RICEBALL).setOptionalIngredients(SALT);//Shop
        addPotRecipe(Meal.STEW, 1.5F, 1.5F, MILK, FLOUR).setOptionalIngredients(EGGPLANT, ONION, POTATO, CARROT, GREEN_PEPPER, FISH, SALT); //Katlin 10000RP
        addPotRecipe(Meal.STEW_PUMPKIN, 2F, 1.4F, PUMPKIN).setOptionalIngredients(SUGAR, SALT);//Shop
        addPotRecipe(Meal.STEW_FISH, 3F, 2F, FISH).setOptionalIngredients(SALT); //Jacob 5000RP
        //Added in 0.6+
        addPotRecipe(Meal.JAM_STRAWBERRY, 1.5F, 0.9F, STRAWBERRY).setOptionalIngredients(WINE); //Goddess 5000RP
        addPotRecipe(Meal.JAM_APPLE, 1.5F, 0.9F, APPLE).setOptionalIngredients(WINE); //Jade 20000RP
        addPotRecipe(Meal.JAM_GRAPE, 1.5F, 0.9F, GRAPE).setOptionalIngredients(WINE);
        addPotRecipe(Meal.MARMALADE, 1.5F, 0.9F, ORANGE).setOptionalIngredients(WINE);
        addPotRecipe(Meal.NOODLES_TEMPURA, 1.1F, 0.9F, TEMPURA, NOODLES);
        addPotRecipe(Meal.RICE_TEMPURA, 1.1F, 0.9F, TEMPURA, RICEBALL);
        //Vanilla style
        addPotRecipe("rabbit_stew", new ItemStack(Items.RABBIT_STEW), BAKED_POTATO, CARROT, RABBIT_COOKED, MUSHROOM);
        addPotRecipe("brown_mushroom", new ItemStack(Items.MUSHROOM_STEW), RED_MUSHROOM, BROWN_MUSHROOM);
    }

    private static void addOvenRecipes() {
        //Added in 0.5+
        addOvenRecipe(Meal.CORN_BAKED, 2F, 1.5F, CORN).setOptionalIngredients(OIL, BUTTER, SALT); //Ashlee 500RP
        addOvenRecipe(Meal.RICEBALLS_TOASTED, 3F, 1.5F, RICEBALL).setOptionalIngredients(SUGAR, SALT);//Shop
        addOvenRecipe(Meal.TOAST, 1.1F, 1.1F, BREAD).setOptionalIngredients(BUTTER); //Jade 5000RP
        addOvenRecipe(Meal.DINNERROLL, 1.2F, 1.1F, EGG, MILK, BUTTER); //Tiberius 5000RP
        addOvenRecipe(Meal.DORIA, 1.1F, 1.1F, ONION, BUTTER, MILK, RICEBALL, FLOUR);//Shop
        addOvenRecipe(Meal.COOKIES, 1.2F, 0.4F, EGG, FLOUR, BUTTER).setOptionalIngredients(SUGAR); //Liara 7500RP
        addOvenRecipe(Meal.COOKIES_CHOCOLATE, 1.1F, 0.5F, COOKIES, CHOCOLATE); //Liara 10000RP
        addOvenRecipe(Meal.CAKE_CHOCOLATE, 1.3F, 1.1F, EGG, FLOUR, BUTTER, CHOCOLATE).setOptionalIngredients(SUGAR, FRUITS); //Yulif RP 100000
        //Added in 0.6+
        addOvenRecipe(Meal.BUN_JAM, 1.1F, 1.2F, MILK, EGG, JAM); //Jade 25000RP
        addOvenRecipe(Meal.SWEET_POTATOES, 1.4F, 1.3F, EGG, BUTTER, SWEET_POTATO);
        addOvenRecipe(Meal.CAKE, 1.3F, 1.1F, EGG, FLOUR, BUTTER, CAKE_FRUIT).setOptionalIngredients(ORANGE, PINEAPPLE, STRAWBERRY, PEACH, GRAPE);
        addOvenRecipe(Meal.PIE_APPLE, 1.2F, 1.1F, APPLE, EGG, BUTTER, FLOUR); //Katlin 20000RP
        //Vanilla style
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
