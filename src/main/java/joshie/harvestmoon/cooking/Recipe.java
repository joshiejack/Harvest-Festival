package joshie.harvestmoon.cooking;

import java.util.List;

public class Recipe {
    public final Meal result;
    private final Ingredient[] requiredIngredients;

    //Optional Extras
    private Utensil requiredTool;
    private Ingredient[] optionalIngredients;

    public Recipe(Ingredient[] ingredients, Meal result) {
        this.requiredIngredients = ingredients;
        this.result = result;
    }

    public Recipe setOptionalIngredients(Ingredient... ingredients) {
        this.optionalIngredients = ingredients;
        return this;
    }

    public Recipe setRequiredTool(Utensil tool) {
        this.requiredTool = tool;
        return this;
    }

    private boolean recipeHasThisIngredient(Ingredient ingredient) {
        //First we check if the Required Ingredients have this ingredient in them
        for (Ingredient i : requiredIngredients) {
            if (i.isEqual(ingredient)) return true;
        }

        //Second we check if the Optional Ingredients have this ingredient in them
        if (optionalIngredients != null) {
            for (Ingredient i : optionalIngredients) {
                if (i.isEqual(ingredient)) return true;
            }
        }

        //Since neither the required ingredients or optional ones, had this recipe. Then the recipe should fail.
        return false;
    }

    private boolean ingredientListContains(List<Ingredient> ingredients, Ingredient required) {
        //Now we should loop through all the ingredient passed in
        for (Ingredient passed : ingredients) {
            if (required.isEqual(passed)) return true; //If we found this ingredient in the list, then we can return it as true;
        }

        return false; //We did not find the item, therefore we return false
    }

    public Meal getMeal(Utensil utensil, List<Ingredient> ingredients) {
        if (ingredients == null || ingredients.size() < 1 || utensil != requiredTool) return null; //If we have no utensils, or not enough recipes remove them all

        /** Step one.
         *  Validate that all supplied Ingredients are Allowed in this Meal.*/
        for (Ingredient ingredient : ingredients) { //Loop through all the ingredients to CHECK if the recipe allow this type of food in to it
            if (!recipeHasThisIngredient(ingredient)) return null; //If the recipe DOES not contain this ingredient then we should return null.
        }

        /** Step two.
         *  Now that we know that all the ingredients are valid ingredients for this recipe.
         *  We need to actually check that we HAVE all of the required ingredients.
         */
        for (Ingredient required : requiredIngredients) { //Loop through the required ingredients
            //If the ingredients list does NOT contain this item we should return null
            if (!ingredientListContains(ingredients, required)) return null;
        }

        /** Final step is to build the meal **/
        Meal meal = new Meal(result);
        if (optionalIngredients != null) { //Loop through optional ingredients
            for (Ingredient optional : optionalIngredients) {
                if (ingredientListContains(ingredients, optional)) { //If the optional ingredients list has this item
                    meal.addIngredient(optional);
                }
            }
        }

        return meal;
    }

    public Meal getBestMeal() {
        Meal meal = new Meal(result);

        if (optionalIngredients != null) {
            for (Ingredient i : optionalIngredients) {
                meal.addIngredient(i);
            }
        }

        return meal;
    }
}
