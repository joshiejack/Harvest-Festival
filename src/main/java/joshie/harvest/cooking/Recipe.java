package joshie.harvest.cooking;

import java.util.HashSet;
import java.util.Set;

import joshie.harvest.api.cooking.ICookingComponent;
import joshie.harvest.api.cooking.IMeal;
import joshie.harvest.api.cooking.IMealRecipe;
import joshie.harvest.api.cooking.IUtensil;

public class Recipe implements IMealRecipe {
    public final IMeal result;
    private final ICookingComponent[] requiredIngredients;

    //Optional Extras
    private IUtensil requiredTool;
    private ICookingComponent[] optionalIngredients;

    public Recipe(ICookingComponent[] ingredients, IMeal result) {
        this.requiredIngredients = ingredients;
        this.result = result;
    }

    @Override
    public IMealRecipe setOptionalIngredients(ICookingComponent... ingredients) {
        this.optionalIngredients = ingredients;
        return this;
    }

    @Override
    public IMealRecipe setRequiredTool(IUtensil tool) {
        this.requiredTool = tool;
        return this;
    }
    

    @Override
    public IMealRecipe setIsDrink() {
        this.result.setIsDrink();
        return this;
    }
    
    @Override
    public IMealRecipe setHasAltTexture() {
        this.result.setHasAltTexture();
        return this;
    }

    private boolean recipeHasThisIngredient(ICookingComponent ingredient) {        
        //First we check if the Required Ingredients have this ingredient in them
        for (ICookingComponent i : requiredIngredients) {
            if (i.isEqual(ingredient)) {
                return true;
            }
        }

        //Second we check if the Optional Ingredients have this ingredient in them
        if (optionalIngredients != null) {
            for (ICookingComponent i : optionalIngredients) {
                if (i.isEqual(ingredient)) return true;
            }
        }

        //Since neither the required ingredients or optional ones, had this recipe. Then the recipe should fail.
        return false;
    }

    private boolean ingredientListContains(Set<ICookingComponent> ingredients, ICookingComponent required) {
        //Now we should loop through all the ingredient passed in
        for (ICookingComponent passed : ingredients) {
            if (required.isEqual(passed)) return true; //If we found this ingredient in the list, then we can return it as true;
        }

        return false; //We did not find the item, therefore we return false
    }
    
    @Override
    public IMeal getMeal(IUtensil utensil, HashSet<ICookingComponent> ingredients) {
        if (ingredients == null || ingredients.size() < 1 || utensil != requiredTool) return null; //If we have no utensils, or not enough recipes remove them all
               
        /** Step one.
         *  Validate that all supplied Ingredients are Allowed in this Meal.*/
        for (ICookingComponent ingredient : ingredients) { //Loop through all the ingredients to CHECK if the recipe allow this type of food in to it
            if (!recipeHasThisIngredient(ingredient)) return null; //If the recipe DOES not contain this ingredient then we should return null.
        }
        
        /** Step two.
         *  Now that we know that all the ingredients are valid ingredients for this recipe.
         *  We need to actually check that we HAVE all of the required ingredients.
         */
        for (ICookingComponent required : requiredIngredients) { //Loop through the required ingredients
            //If the ingredients list does NOT contain this item we should return null
            if (!ingredientListContains(ingredients, required)) return null;
        }
        
        /** Final step is to build the meal **/
        IMeal meal = new Meal(result);
        if (optionalIngredients != null) { //Loop through optional ingredients
            for (ICookingComponent optional : optionalIngredients) {
                if (ingredientListContains(ingredients, optional)) { //If the optional ingredients list has this item
                    meal.addIngredient(optional);
                }
            }
        }

        return meal;
    }
    
    @Override
    public IMeal getMeal() {
        return new Meal(result); //Clone the meal
    }

    @Override
    public IMeal getBestMeal() {
        IMeal meal = getMeal();
        if (optionalIngredients != null) {
            for (ICookingComponent i : optionalIngredients) {
                meal.addIngredient(i);
            }
        }

        return meal;
    }
}
