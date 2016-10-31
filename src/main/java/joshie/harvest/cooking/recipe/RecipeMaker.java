package joshie.harvest.cooking.recipe;

import joshie.harvest.api.cooking.CookingHandler;
import joshie.harvest.api.cooking.IngredientStack;
import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.api.cooking.Utensil;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;


public class RecipeMaker implements CookingHandler {
    public static final RecipeBuilder BUILDER = new RecipeBuilder();

    @Nullable
    @Override
    public List<ItemStack> getResult(Utensil utensil, List<ItemStack> stacks, List<IngredientStack> ingredients) {
        for (Recipe recipe: Recipe.REGISTRY) {
            List<ItemStack> ret = findRecipe(utensil, recipe, ingredients);
            if (ret != null) {
                return ret;
            }
        }

        return null;
    }

    private List<ItemStack> findRecipe(Utensil utensil, Recipe recipe, List<IngredientStack> ingredients) {
        if (recipe.getUtensil() != utensil) return null;
        if (!areIngredientsAllowedInRecipe(recipe, ingredients)) return null;
        if (!areAllRequiredInRecipe(recipe.getRequired(), ingredients)) return null;
        return BUILDER.build(recipe, ingredients);
    }

    public static boolean areAllRequiredInRecipe(Collection<IngredientStack> requiredSet, Collection<IngredientStack> ingredients) {
        for (IngredientStack required: requiredSet) if (!required.isSame(ingredients)) return false;
        return true; //Return true because they were all in the recipe!
    }

    private boolean isInRecipe(Recipe recipe, IngredientStack stack) {
        for (IngredientStack required: recipe.getRequired()) if (required.isSame(stack)) return true;
        for (IngredientStack optional: recipe.getOptional()) if (optional.isSame(stack)) return true;
        return false; //We didn't find this stack in the recipe
    }

    private boolean areIngredientsAllowedInRecipe(Recipe recipe, List<IngredientStack> ingredients) {
        for (IngredientStack ingredient: ingredients) if (!isInRecipe(recipe, ingredient)) return false;
        return true; //return true because they're all allowed in!
    }
}
