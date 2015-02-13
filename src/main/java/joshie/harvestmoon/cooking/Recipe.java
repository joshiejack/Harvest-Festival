package joshie.harvestmoon.cooking;

import java.util.List;

public class Recipe {
    public final Meal result;
    private final Ingredient[] requiredIngredients;

    //Optional Extras
    private Utensil requiredTool;
    private Ingredient[] optionalIngredients;
    private Seasoning[] requiredSeasonings;
    private Seasoning[] optionalSeasonings;

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

    public Recipe setRequiredSeasonings(Seasoning... seasoning) {
        this.requiredSeasonings = seasoning;
        return this;
    }

    public Recipe setOptionalSeasonings(Seasoning... seasoning) {
        this.optionalSeasonings = seasoning;
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

    private boolean recipeHasThisSeasoning(Seasoning seasoning) {
        //First we check if the Required Seasonings have this ingredient in them
        if (requiredSeasonings != null) {
            for (Seasoning s : requiredSeasonings) {
                if (s.isEqual(seasoning)) return true;
            }
        }

        //Second we check if the Optional Seasonings have this seasoning in them
        if (optionalSeasonings != null) {
            for (Seasoning s : optionalSeasonings) {
                if (s.isEqual(seasoning)) return true;
            }
        }

        //Since this seasoning couldn't be found in required or optional, you should screw the meal up
        return false;
    }

    private boolean ingredientListContains(List<Ingredient> ingredients, Ingredient required) {
        //Now we should loop through all the ingredient passed in
        for (Ingredient passed : ingredients) {
            if (required.isEqual(passed)) return true; //If we found this ingredient in the list, then we can return it as true;
        }

        return false; //We did not find the item, therefore we return false
    }

    private boolean seasoningListContains(List<Seasoning> seasonings, Seasoning required) {
        for (Seasoning passed : seasonings) {
            if (required.isEqual(passed)) return true;
        }

        return false;
    }

    public Meal getMeal(Utensil utensil, List<Ingredient> ingredients, List<Seasoning> seasonings) {
        if (ingredients == null || ingredients.size() < 1 || utensil != requiredTool) return null; //If we have no utensils, or not enough recipes remove them all

        /** Step one.
         *  Validate that all supplied Ingredients are Allowed in this Meal.
         */
        for (Ingredient ingredient : ingredients) { //Loop through all the ingredients to CHECK if the recipe allow this type of food in to it
            if (!recipeHasThisIngredient(ingredient)) return null; //If the recipe DOES not contain this ingredient then we should return null.
        }

        /** Step two.
         *  Validate that all supplied seasonings are allowed in this meal
         */
        if (seasonings != null) {
            for (Seasoning seasoning : seasonings) { //This is a clone of the ingredient method.
                if (!recipeHasThisSeasoning(seasoning)) return null;
            }
        }

        /** Step three.
         *  Now that we know that all the ingredients are valid ingredients for this recipe.
         *  We need to actually check that we HAVE all of the required ingredients.
         */
        for (Ingredient required : requiredIngredients) { //Loop through the required ingredients
            //If the ingredients list does NOT contain this item we should return null
            if (!ingredientListContains(ingredients, required)) return null;
        }

        /** Step four
         *  Perform the same task for requiredSeasonings. If we need to check them
         */
        if (requiredSeasonings != null) {
            for (Seasoning required : requiredSeasonings) { //Loop through the required seasonings
                //If the seasonings list does NOT contain this item we should return null
                if (!seasoningListContains(seasonings, required)) return null;
            }
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

        if (optionalSeasonings != null) { //Loop through optional ingredients
            for (Seasoning optional : optionalSeasonings) {
                if (seasoningListContains(seasonings, optional)) { //If the optional ingredients list has this item
                    meal.addSeasoning(optional);
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

        //Add the bonuses for having the optional seasonings to this meal
        if (optionalSeasonings != null) {
            for (Seasoning s : optionalSeasonings) {
                meal.addSeasoning(s);
            }
        }

        return meal;
    }
}
