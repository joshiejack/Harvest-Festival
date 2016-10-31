package joshie.harvest.cooking.recipe;

import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.api.cooking.IngredientStack;
import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.api.cooking.Utensil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import static joshie.harvest.api.cooking.Utensil.*;
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
                    if (internalHunger == 0 || test.getHunger() < internalHunger) {
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

        hunger = (int)Math.max(1, hunger * hungerModifier);
        saturation = Math.max(0.2F, saturation * modifier);

        //Add the recipe
        Recipe recipe = new RecipeHF(utensil, hunger, saturation, toIngredientStacks(ingredients)).setEatTimer(eatTimer);
        recipe.setRegistryName(new ResourceLocation(MODID, mealname));
        Recipe.REGISTRY.register(recipe);
        return recipe;
    }

    private static Recipe addRecipe(String name, ItemStack result, Utensil utensil, Ingredient... ingredients) {
        Recipe recipe = new RecipeVanilla(result, utensil, toIngredientStacks(ingredients));
        recipe.setRegistryName(new ResourceLocation(MODID, name));
        Recipe.REGISTRY.register(recipe);
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
    
    public static Recipe addFryingPanRecipe(String mealname, float hungerModifier, float modifier, Ingredient... ingredients) {
        return addRecipe(mealname, FRYING_PAN, hungerModifier, modifier, false, ingredients);
    }

    public static void addFryingPanRecipe(String mealname, ItemStack result, Ingredient... ingredients) {
        addRecipe(mealname, result, FRYING_PAN, ingredients);
    }
    
    public static Recipe addMixerRecipe(String mealname, boolean isDrink, float hungerModifier, float modifier, Ingredient... ingredients) {
        return addRecipe(mealname, MIXER, hungerModifier, modifier, isDrink, ingredients);
    }
    
    public static Recipe addMixerRecipe(String mealname, float hungerModifier, float modifier, Ingredient... ingredients) {
        return addRecipe(mealname, MIXER, hungerModifier, modifier, true, ingredients);
    }

    public static void addMixerRecipe(String mealname, ItemStack result, Ingredient... ingredients) {
        addRecipe(mealname, result, MIXER, ingredients);
    }
    
    public static Recipe addNoUtensilRecipe(String mealname, float hungerModifier, float modifier, Ingredient... ingredients) {
        return addRecipe(mealname, COUNTER, hungerModifier, modifier, false, ingredients);
    }

    public static void addNoUtensilRecipe(String mealname, ItemStack result, Ingredient... ingredients) {
        addRecipe(mealname, result, COUNTER, ingredients);
    }
    
    public static Recipe addPotRecipe(String mealname, boolean isDrink, float hungerModifier, float modifier, Ingredient... ingredients) {
        return addRecipe(mealname, POT, hungerModifier, modifier, isDrink, ingredients);
    }
    
    public static Recipe addPotRecipe(String mealname, float hungerModifier, float modifier, Ingredient... ingredients) {
        return addRecipe(mealname, POT, hungerModifier, modifier, false, ingredients);
    }

    public static void addPotRecipe(String mealname, ItemStack result, Ingredient... ingredients) {
        addRecipe(mealname, result, POT, ingredients);
    }
    
    public static Recipe addOvenRecipe(String mealname, float hungerModifier, float modifier, Ingredient... ingredients) {
        return addRecipe(mealname, OVEN, hungerModifier, modifier, false, ingredients);
    }

    public static void addOvenRecipe(String mealname, ItemStack result, Ingredient... ingredients) {
        addRecipe(mealname, result, OVEN, ingredients);
    }
}
