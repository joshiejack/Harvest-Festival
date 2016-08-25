package joshie.harvest.cooking.recipe;

import joshie.harvest.api.cooking.ICookingIngredient;
import joshie.harvest.api.cooking.ISpecialRecipeHandler;
import joshie.harvest.cooking.Utensil;
import net.minecraft.item.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecipeStack implements ISpecialRecipeHandler {
    public static final RecipeStack INSTANCE = new RecipeStack();
    private final Set<RecipeVanilla> recipes = new HashSet<>();

    private RecipeStack() {}

    @Override
    public ItemStack getResult(Utensil utensil, List<ItemStack> stacks, List<ICookingIngredient> ingredients) {
        for (RecipeVanilla recipe : recipes) {
            ItemStack ret = recipe.getResult(utensil, ingredients);
            if (ret != null) {
                return ret.copy();
            }
        }

        return null;
    }

    public void addRecipe(ItemStack output, Utensil utensil, ICookingIngredient... ingredients) {
        recipes.add(new RecipeVanilla(utensil, output, ingredients));
    }

    private static class RecipeVanilla {
        public final ItemStack result;
        private final ICookingIngredient[] requiredIngredients;
        private Utensil requiredTool;

        private RecipeVanilla(Utensil utensil, ItemStack result, ICookingIngredient... ingredients) {
            this.requiredTool = utensil;
            this.requiredIngredients = ingredients;
            this.result = result;
        }

        private boolean recipeHasThisIngredient(ICookingIngredient ingredient) {
            //First we check if the Required Ingredients have this ingredient in them
            for (ICookingIngredient i : requiredIngredients) {
                if (i.isEqual(ingredient)) {
                    return true;
                }
            }

            //Since neither the required ingredients or optional ones, had this recipe. Then the recipe should fail.
            return false;
        }

        private boolean ingredientListContains(List<ICookingIngredient> ingredients, ICookingIngredient required) {
            //Now we should loop through all the ingredient passed in
            for (ICookingIngredient passed : ingredients) {
                if (required.isEqual(passed))
                    return true; //If we found this ingredient in the list, then we can return it as true;
            }

            return false; //We did not find the item, therefore we return false
        }

        public ItemStack getResult(Utensil utensil, List<ICookingIngredient> ingredients) {
            if (ingredients == null || ingredients.size() < 1 || utensil != requiredTool)
                return null; //If we have no utensils, or not enough recipes remove them all

            /** Step one.
             *  Validate that all supplied Ingredients are Allowed in this Meal.*/
            for (ICookingIngredient ingredient : ingredients) { //Loop through all the ingredients to CHECK if the recipe allow this type of food in to it
                if (!recipeHasThisIngredient(ingredient))
                    return null; //If the recipe DOES not contain this ingredient then we should return null.
            }

            /** Step two.
             *  Now that we know that all the ingredients are valid ingredients for this recipe.
             *  We need to actually check that we HAVE all of the required ingredients.
             */
            for (ICookingIngredient required : requiredIngredients) { //Loop through the required ingredients
                //If the ingredients list does NOT contain this item we should return null
                if (!ingredientListContains(ingredients, required)) return null;
            }

            /** Final step is to return sthe stack **/
            return result;
        }
    }
}
