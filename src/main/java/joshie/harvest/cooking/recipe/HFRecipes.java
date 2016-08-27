package joshie.harvest.cooking.recipe;

import joshie.harvest.api.cooking.ICookingIngredient;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.Meal;
import joshie.harvest.cooking.items.ItemIngredients;
import joshie.harvest.core.util.HFLoader;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static joshie.harvest.cooking.HFCooking.INGREDIENTS;
import static joshie.harvest.cooking.recipe.HFIngredients.*;
import static joshie.harvest.core.helpers.RecipeHelper.*;

@HFLoader
public class HFRecipes {
    public static final Recipe NULL_RECIPE = new Recipe("null", new ICookingIngredient[0], new Meal(0, 0F, 0F, 0));

    public static void preInit() {
        //Recipes ; http://fogu.com/hm4/farm/stamina_chart.htm for numbers
        //Hunger = Stamina / 3.5F, Saturation = Stamina/35, Exhaustion = Fatigue * 2
        //Frying Pan
        addFryingPanRecipe("pancake_savoury", 14, 1.43F, -2F, FLOUR, CABBAGE, OIL, EGG).setOptionalIngredients(ONION);
        addFryingPanRecipe("fries_french", 4, 0.43F, 0F, POTATO, OIL).setOptionalIngredients(SALT);
        addFryingPanRecipe("popcorn", 9, 0.86F, -2F, CORN).setOptionalIngredients(BUTTER, SALT);
        addFryingPanRecipe("cornflakes", 3, 0.29F, -4F, CORN, MILK).setOptionalIngredients(SUGAR);
        addFryingPanRecipe("eggplant_happy", 8, 0.85F, -4F, EGGPLANT).setOptionalIngredients(SUGAR);
        addFryingPanRecipe("egg_scrambled", 11, 1.14F, -6F, EGG, OIL).setOptionalIngredients(BUTTER, MAYONNAISE, SALT).setAlternativeTexture(INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.EGG_SCRAMBLED));
        addFryingPanRecipe("omelet", 14, 1.43F, -8F, EGG, OIL, MILK).setOptionalIngredients(SALT);
        addFryingPanRecipe("omelet_rice", 17, 1.71F, -8F, EGG, MILK, OIL, RICEBALL).setOptionalIngredients(CABBAGE, ONION, MUSHROOM, GREEN_PEPPER, SALT);
        addFryingPanRecipe("toast_french", 9, 0.86F, -4F, EGG, BREAD, OIL, SUGAR).setOptionalIngredients(BUTTER);
        addFryingPanRecipe("doughnut", 9, 0.86F, -4F, EGG, MILK, BUTTER, FLOUR, OIL);
        addFryingPanRecipe("fish_grilled", 9, 1.14F, -2F, FISH, OIL, SALT);
        addFryingPanRecipe("pancake", 6, 0.57F, -6F, EGG, MILK, FLOUR, OIL).setOptionalIngredients(SUGAR, BUTTER);
        addFryingPanRecipe("potsticker", 7, 0.71F, -2F, CABBAGE, ONION, FLOUR, OIL);
        addFryingPanRecipe("risotto", 10, 1.0F, -2F, TOMATO, ONION, RICEBALL, OIL);

        //Mixer
        addMixerRecipe("juice_pineapple", 2, 1.43F, -30F, PINEAPPLE).setOptionalIngredients(SALT, SUGAR);
        addMixerRecipe("juice_tomato", 6, 0.57F, -40F, TOMATO).setOptionalIngredients(SALT);
        addMixerRecipe("milk_strawberry", 9, 0.86F, -30F, STRAWBERRY, MILK).setOptionalIngredients(SUGAR);
        addMixerRecipe("juice_vegetable", 6, 0.57F, -40F, JUICE_VEGETABLE).setOptionalIngredients(CUCUMBER, ONION, CABBAGE, TOMATO, SPINACH, CARROT, GREEN_PEPPER, TURNIP, SALT); //Yo this doesnt make any sense. It requires Vegetable Juice to make iteslf?
        addMixerRecipe("latte_vegetable", 9, 0.86F, -40F, JUICE_VEGETABLE, MILK).setOptionalIngredients(CUCUMBER, ONION, CABBAGE, TOMATO, SPINACH, CARROT, GREEN_PEPPER, TURNIP, SALT);
        addMixerRecipe("ketchup", 1, 0.03F, 0F, TOMATO, ONION).setOptionalIngredients(SALT, SUGAR).setAlternativeTexture(INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.KETCHUP));
        addMixerRecipe("butter", false, 1, 0.03F, 0F, MILK).setOptionalIngredients(SALT).setAlternativeTexture(INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.BUTTER));
        addMixerRecipe("fishsticks", false, 1, 0.14F, -2F, FISH).setOptionalIngredients(SALT);

        //Vanilla Mix Recipes
        addMixerRecipe(new ItemStack(Items.BEETROOT_SOUP), BEETROOT, TOMATO, ONION, OIL);
        addMixerRecipe(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.FLOUR), WHEAT);

        //Hand
        addNoUtensilRecipe("turnip_pickled", 2, 0.17F, -4F, TURNIP).setOptionalIngredients(SALT); //Cafe Reward
        addNoUtensilRecipe("cucumber_pickled", 2, 0.17F, -3F, CUCUMBER).setOptionalIngredients(SALT);
        addNoUtensilRecipe("salad", 3, 0.29F, -6F, SALAD_INGREDIENT).setOptionalIngredients(MUSHROOM, CUCUMBER, CABBAGE, TOMATO, CARROT, SALT);
        addNoUtensilRecipe("sandwich", 2, 0.23F, -4F, BREAD, SANDWICH_INGREDIENT).setOptionalIngredients(BUTTER, TOMATO, CUCUMBER, SALT, MAYONNAISE, MUSHROOM);
        addNoUtensilRecipe("sushi", 9, 0.86F,  -10F, SASHIMI, RICEBALL);
        addNoUtensilRecipe("sashimi", 6, 0.63F, -8F, FISH).setAlternativeTexture(INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.SASHIMI));
        addNoUtensilRecipe("sashimi_chirashi", 14, 1.43F, -14F, SASHIMI, SCRAMBLED_EGG, RICEBALL, SASHIMI_VEGETABLE);

        //Pot
        addPotRecipe("milk_hot", true, 6, 0.57F, -20F, MILK).setOptionalIngredients(SUGAR);
        addPotRecipe("chocolate_hot", true, 3, 0.29F, -30F, MILK, CHOCOLATE).setOptionalIngredients(SUGAR);
        addPotRecipe("egg_boiled", 6, 0.57F, -4F, EGG).setOptionalIngredients(SALT);
        addPotRecipe("spinach_boiled", 6, 0.57F, -2F, SPINACH);
        addPotRecipe("potato_candied", 2, 0.23F, -2F, SWEET_POTATO).setOptionalIngredients(SUGAR);
        addPotRecipe("dumplings", true, 7, 0.71F, -10F, CABBAGE, ONION, FLOUR, OIL).setOptionalIngredients(SUGAR);
        addPotRecipe("noodles", 13, 1.14F, -6F, FLOUR).setOptionalIngredients(SALT);
        addPotRecipe("soup_rice", 3, 0.29F, -4F, RICEBALL);
        addPotRecipe("porridge", 2, 0.23F, -4F, MILK, RICEBALL).setOptionalIngredients(SUGAR);
        addPotRecipe("egg_overrice", 6, 0.34F, -2F, EGG, RICEBALL).setOptionalIngredients(SALT);
        addPotRecipe("stew", 9, 0.86F, -2F, MILK, FLOUR).setOptionalIngredients(EGGPLANT, ONION, POTATO, CARROT, GREEN_PEPPER, FISH, SALT);
        addPotRecipe("stew_pumpkin", true, 2, 0.23F, -2F, PUMPKIN).setOptionalIngredients(SUGAR, SALT);
        addPotRecipe("stew_fish", 2, 0.2F, -2F, FISH).setOptionalIngredients(SALT);

        //Vanilla Pot Recipes
        addPotRecipe(new ItemStack(Items.RABBIT_STEW), BAKED_POTATO, CARROT, RABBIT_COOKED, MUSHROOM);
        addPotRecipe(new ItemStack(Items.MUSHROOM_STEW), RED_MUSHROOM, BROWN_MUSHROOM);

        //Oven
        addOvenRecipe("corn_baked", 2, 0.2F, -2F, CORN).setOptionalIngredients(OIL, BUTTER, SALT);
        addOvenRecipe("riceballs_toasted", 3, 0.34F, -2F, RICEBALL).setOptionalIngredients(SUGAR, SALT);
        addOvenRecipe("toast", 5, 0.46F, -2F, BREAD).setOptionalIngredients(BUTTER);
        addOvenRecipe("dinnerroll", 3, 0.26F, -4F, EGG, MILK, BUTTER);
        addOvenRecipe("doria", 7, 0.71F, -6F, ONION, BUTTER, MILK, RICEBALL, FLOUR);
        addOvenRecipe("cookies", 4, 0.43F, -10F, EGG, FLOUR, BUTTER).setOptionalIngredients(SUGAR).setAlternativeTexture(INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.COOKIES));
        addOvenRecipe("cookies_chocolate", 9, 0.86F, -10F, COOKIES, CHOCOLATE);
        addOvenRecipe("cake_chocolate", 20, 2.0F, -16F, EGG, FLOUR, BUTTER, CHOCOLATE).setOptionalIngredients(SUGAR, PINEAPPLE, APPLE, STRAWBERRY);

        //Vanilla Oven Recipes
        addOvenRecipe(new ItemStack(Items.BREAD), FLOUR);
        addOvenRecipe(new ItemStack(Items.BAKED_POTATO), POTATO);
        addOvenRecipe(new ItemStack(Items.COOKED_CHICKEN), CHICKEN);
        addOvenRecipe(new ItemStack(Items.COOKED_BEEF), BEEF);
        addOvenRecipe(new ItemStack(Items.COOKED_PORKCHOP), PORK);
        addOvenRecipe(new ItemStack(Items.COOKED_MUTTON), MUTTON);
        addOvenRecipe(new ItemStack(Items.COOKED_RABBIT), RABBIT);
        addOvenRecipe(new ItemStack(Items.COOKED_FISH, 1, 0), COD);
        addOvenRecipe(new ItemStack(Items.COOKED_FISH, 1, 1), SALMON);
        addOvenRecipe(new ItemStack(Items.PUMPKIN_PIE), PUMPKIN, SUGAR, EGG);
    }
}
