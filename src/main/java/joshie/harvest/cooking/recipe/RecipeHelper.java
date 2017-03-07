package joshie.harvest.cooking.recipe;

import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.api.cooking.IngredientStack;
import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.cooking.CookingAPI;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class RecipeHelper {
    private static Recipe addRecipe(String mealname, Utensil utensil, float hungerModifier, float modifier, int eatTimer, Ingredient... ingredients) {
        //Time to calculate hunger and saturation from the base ingredients
        int hunger = 0;
        float saturation = 0F;
        for (Ingredient ingredient: ingredients) {
            int internalHunger = ingredient.getHunger();
            if (internalHunger == 0) {
                float internalSaturation = 0F;
                for (Ingredient test : ingredient.getEquivalents()) {
                    if (internalHunger == 0 || (test.getSellValue() != 0) && test.getHunger() < internalHunger) {
                        internalHunger = test.getHunger();
                        internalSaturation = test.getSaturation();
                    }
                }

                hunger += internalHunger;
                saturation += internalSaturation;
            } else {
                hunger += ingredient.getHunger();
                saturation += ingredient.getSaturation();
            }
        }

        hunger = (int)Math.min(20, Math.max(1, hunger * hungerModifier));
        saturation = Math.min(1.6F, Math.max(0.2F, saturation * modifier));

        //Add the recipe
        ResourceLocation resource = new ResourceLocation(MODID, mealname);
        Recipe recipe = new RecipeHF(resource, utensil, hunger, saturation, toIngredientStacks(ingredients)).setEatTimer(eatTimer);
        //TODO: Remove in 0.7+
        recipe.setRegistryName(resource);
        //TODO: Remove in 0.7+
        CookingAPI.REGISTRY.register(recipe);
        return recipe;
    }

    private static Recipe addRecipe(String name, ItemStack result, Utensil utensil, Ingredient... ingredients) {
        ResourceLocation resource = new ResourceLocation(MODID, name);
        Recipe recipe = new RecipeVanilla(resource, result, utensil, toIngredientStacks(ingredients));
        //TODO: Remove in 0.7+
        recipe.setRegistryName(resource);
        //TODO: Remove in 0.7+
        CookingAPI.REGISTRY.register(recipe);
        return recipe;
    }

    public static IngredientStack[] toIngredientStacks(Ingredient[] ingredients) {
        IngredientStack[] stacks = new IngredientStack[ingredients.length];
        for (int i = 0; i < ingredients.length; i++) {
            stacks[i] = new IngredientStack(ingredients[i], 1);
        }

        return stacks;
    }

    private static Recipe addRecipe(String mealname, Utensil utensil, float hungerModifier, float modifier, boolean drink, Ingredient... ingredients) {
        int timer = drink ? 8:  24 * (int) modifier;
        Recipe ret = addRecipe(mealname, utensil, hungerModifier, modifier, timer, ingredients);
        if(drink) ret.setIsDrink();
        return ret;
    }
    
    public static Recipe addFryingPanRecipe(Meal meal, float hungerModifier, float modifier, Ingredient... ingredients) {
        return addRecipe(meal.getName(), HFCooking.FRYING_PAN, hungerModifier, modifier, false, ingredients);
    }

    @SuppressWarnings("unused")
    public static void addFryingPanRecipe(String mealname, ItemStack result, Ingredient... ingredients) {
        addRecipe(mealname, result, HFCooking.FRYING_PAN, ingredients);
    }

    public static Recipe addMixerRecipe(Meal meal, boolean isDrink, float hungerModifier, float modifier, Ingredient... ingredients) {
        return addRecipe(meal.getName(), HFCooking.MIXER, hungerModifier, modifier, isDrink, ingredients);
    }
    
    public static Recipe addMixerRecipe(Meal meal, float hungerModifier, float modifier, Ingredient... ingredients) {
        return addRecipe(meal.getName(), HFCooking.MIXER, hungerModifier, modifier, true, ingredients);
    }

    public static Recipe addMixerRecipe(String mealname, ItemStack result, Ingredient... ingredients) {
        return addRecipe(mealname, result, HFCooking.MIXER, ingredients);
    }
    
    public static Recipe addNoUtensilRecipe(Meal meal, float hungerModifier, float modifier, Ingredient... ingredients) {
        return addRecipe(meal.getName(), HFCooking.COUNTER, hungerModifier, modifier, false, ingredients);
    }

    public static Recipe addNoUtensilRecipe(String mealname, ItemStack result, Ingredient... ingredients) {
        return addRecipe(mealname, result, HFCooking.COUNTER, ingredients);
    }
    
    public static Recipe addPotRecipe(Meal meal, boolean isDrink, float hungerModifier, float modifier, Ingredient... ingredients) {
        return addRecipe(meal.getName(), HFCooking.POT, hungerModifier, modifier, isDrink, ingredients);
    }
    
    public static Recipe addPotRecipe(Meal meal, float hungerModifier, float modifier, Ingredient... ingredients) {
        return addRecipe(meal.getName(), HFCooking.POT, hungerModifier, modifier, false, ingredients);
    }

    public static Recipe addPotRecipe(String mealname, ItemStack result, Ingredient... ingredients) {
        return addRecipe(mealname, result, HFCooking.POT, ingredients);
    }
    
    public static Recipe addOvenRecipe(Meal meal, float hungerModifier, float modifier, Ingredient... ingredients) {
        return addRecipe(meal.getName(), HFCooking.OVEN, hungerModifier, modifier, false, ingredients);
    }

    public static Recipe addOvenRecipe(String mealname, ItemStack result, Ingredient... ingredients) {
        return addRecipe(mealname, result, HFCooking.OVEN, ingredients);
    }
}
