package joshie.harvest.cooking.recipe;

import joshie.harvest.api.cooking.ICookingIngredient;
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
        addFryingPanRecipe("pancake_savoury", 14, 1.43F, -2F, flour, cabbage, oil, egg).setOptionalIngredients(onion);
        addFryingPanRecipe("fries_french", 4, 0.43F, 0F, potato, oil).setOptionalIngredients(salt);
        addFryingPanRecipe("popcorn", 9, 0.86F, -2F, corn).setOptionalIngredients(butter, salt);
        addFryingPanRecipe("cornflakes", 3, 0.29F, -4F, corn, milk).setOptionalIngredients(sugar);
        addFryingPanRecipe("eggplant_happy", 8, 0.85F, -4F, eggplant).setOptionalIngredients(sugar);
        addFryingPanRecipe("egg_scrambled", 11, 1.14F, -6F, egg, oil).setOptionalIngredients(butter, mayonnaise, salt).setAlternativeTexture(INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.EGG_SCRAMBLED));
        addFryingPanRecipe("omelet", 14, 1.43F, -8F, egg, oil, milk).setOptionalIngredients(salt);
        addFryingPanRecipe("omelet_rice", 17, 1.71F, -8F, egg, milk, oil, riceball).setOptionalIngredients(cabbage, onion, MUSHROOM, green_pepper, salt);
        addFryingPanRecipe("toast_french", 9, 0.86F, -4F, egg, bread, oil, sugar).setOptionalIngredients(butter);
        addFryingPanRecipe("doughnut", 9, 0.86F, -4F, egg, milk, butter, flour, oil);
        addFryingPanRecipe("fish_grilled", 9, 1.14F, -2F, FISH, oil, salt);
        addFryingPanRecipe("pancake", 6, 0.57F, -6F, egg, milk, flour, oil).setOptionalIngredients(sugar, butter);
        addFryingPanRecipe("potsticker", 7, 0.71F, -2F, cabbage, onion, flour, oil);
        addFryingPanRecipe("risotto", 10, 1.0F, -2F, tomato, onion, riceball, oil);

        //Mixer
        addMixerRecipe("juice_pineapple", 2, 1.43F, -30F, pineapple).setOptionalIngredients(salt, sugar);
        addMixerRecipe("juice_tomato", 6, 0.57F, -40F, tomato).setOptionalIngredients(salt);
        addMixerRecipe("milk_strawberry", 9, 0.86F, -30F, strawberry, milk).setOptionalIngredients(sugar);
        addMixerRecipe("juice_vegetable", 6, 0.57F, -40F, JUICE_VEGETABLE).setOptionalIngredients(cucumber, onion, cabbage, tomato, spinach, carrot, green_pepper, turnip, salt); //Yo this doesnt make any sense. It requires Vegetable Juice to make iteslf?
        addMixerRecipe("latte_vegetable", 9, 0.86F, -40F, JUICE_VEGETABLE, milk).setOptionalIngredients(cucumber, onion, cabbage, tomato, spinach, carrot, green_pepper, turnip, salt);
        addMixerRecipe("ketchup", 1, 0.03F, 0F, tomato, onion).setOptionalIngredients(salt, sugar).setAlternativeTexture(INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.KETCHUP));
        addMixerRecipe("butter", false, 1, 0.03F, 0F, milk).setOptionalIngredients(salt).setAlternativeTexture(INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.BUTTER));
        addMixerRecipe("fishsticks", false, 1, 0.14F, -2F, FISH).setOptionalIngredients(salt);

        //Vanilla Mix Recipes
        addMixerRecipe(new ItemStack(Items.BEETROOT_SOUP), beetroot);

        //Hand
        addNoUtensilRecipe("turnip_pickled", 2, 0.17F, -4F, turnip).setOptionalIngredients(salt);
        addNoUtensilRecipe("cucumber_pickled", 2, 0.17F, -3F, cucumber).setOptionalIngredients(salt);
        addNoUtensilRecipe("salad", 3, 0.29F, -6F, SALAD_INGREDIENT).setOptionalIngredients(MUSHROOM, cucumber, cabbage, tomato, carrot, salt);
        addNoUtensilRecipe("sandwich", 2, 0.23F, -4F, bread, SANDWICH_INGREDIENT).setOptionalIngredients(butter, tomato, cucumber, salt, mayonnaise, MUSHROOM);
        addNoUtensilRecipe("sushi", 9, 0.86F,  -10F, sashimi, riceball);
        addNoUtensilRecipe("sashimi", 6, 0.63F, -8F, FISH).setAlternativeTexture(INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.SASHIMI));
        addNoUtensilRecipe("sashimi_chirashi", 14, 1.43F, -14F, sashimi, scrambled_egg, riceball, SASHIMI_VEGETABLE);

        //Pot
        addPotRecipe("milk_hot", true, 6, 0.57F, -20F, milk).setOptionalIngredients(sugar);
        addPotRecipe("chocolate_hot", true, 3, 0.29F, -30F, milk, chocolate).setOptionalIngredients(sugar);
        addPotRecipe("egg_boiled", 6, 0.57F, -4F, egg).setOptionalIngredients(salt);
        addPotRecipe("spinach_boiled", 6, 0.57F, -2F, spinach);
        addPotRecipe("potato_candied", 2, 0.23F, -2F, sweet_potato).setOptionalIngredients(sugar);
        addPotRecipe("dumplings", true, 7, 0.71F, -10F, cabbage, onion, flour, oil).setOptionalIngredients(sugar);
        addPotRecipe("noodles", 13, 1.14F, -6F, flour).setOptionalIngredients(salt);
        addPotRecipe("soup_rice", 3, 0.29F, -4F, riceball);
        addPotRecipe("porridge", 2, 0.23F, -4F, milk, riceball).setOptionalIngredients(sugar);
        addPotRecipe("egg_overrice", 6, 0.34F, -2F, egg, riceball).setOptionalIngredients(salt);
        addPotRecipe("stew", 9, 0.86F, -2F, milk, flour).setOptionalIngredients(eggplant, onion, potato, carrot, green_pepper, FISH, salt);
        addPotRecipe("stew_pumpkin", true, 2, 0.23F, -2F, pumpkin).setOptionalIngredients(sugar, salt);
        addPotRecipe("stew_fish", 2, 0.2F, -2F, FISH).setOptionalIngredients(salt);

        //Vanilla Pot Recipes
        addPotRecipe(new ItemStack(Items.RABBIT_STEW), potato, carrot, RABBIT, MUSHROOM);
        addPotRecipe(new ItemStack(Items.MUSHROOM_STEW), red_mushroom, brown_mushroom);

        //Oven
        addOvenRecipe("corn_baked", 2, 0.2F, -2F, corn).setOptionalIngredients(oil, butter, salt);
        addOvenRecipe("riceballs_toasted", 3, 0.34F, -2F, riceball).setOptionalIngredients(sugar, salt);
        addOvenRecipe("toast", 5, 0.46F, -2F, bread).setOptionalIngredients(butter);
        addOvenRecipe("dinnerroll", 3, 0.26F, -4F, egg, milk, butter);
        addOvenRecipe("doria", 7, 0.71F, -6F, onion, butter, milk, riceball, flour);
        addOvenRecipe("cookies", 4, 0.43F, -10F, egg, flour, butter).setOptionalIngredients(sugar).setAlternativeTexture(INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.COOKIES));
        addOvenRecipe("cookies_chocolate", 9, 0.86F, -10F, cookies, chocolate);
        addOvenRecipe("cake_chocolate", 20, 2.0F, -16F, egg, flour, butter, chocolate).setOptionalIngredients(sugar, pineapple, apple, strawberry);

        //Vanilla Oven Recipes
        addOvenRecipe(new ItemStack(Items.BREAD), wheat);
        addOvenRecipe(new ItemStack(Items.BAKED_POTATO), potato);
        addOvenRecipe(new ItemStack(Items.COOKED_CHICKEN), CHICKEN);
        addOvenRecipe(new ItemStack(Items.COOKED_BEEF), BEEF);
        addOvenRecipe(new ItemStack(Items.COOKED_PORKCHOP), PORK);
        addOvenRecipe(new ItemStack(Items.COOKED_MUTTON), MUTTON);
        addOvenRecipe(new ItemStack(Items.COOKED_RABBIT), RABBIT);
        addOvenRecipe(new ItemStack(Items.COOKED_FISH, 1, 0), COD);
        addOvenRecipe(new ItemStack(Items.COOKED_FISH, 1, 1), SALMON);
        addOvenRecipe(new ItemStack(Items.PUMPKIN_PIE), pumpkin, sugar, egg);
    }
}
