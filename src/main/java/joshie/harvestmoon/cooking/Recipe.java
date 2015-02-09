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
    
    private boolean contains(List<ICookingComponent> ingredients, ICookingComponent ingredient) {
        for(ICookingComponent i: ingredients) {
            if(i.isEquivalent(result.unlocalized, ingredient)) {
                return true;
            }
        }
        
        return false;
    }
    
    public Meal getMeal(ICookingComponent utensil, List ingredients, List seasonings) {
        //If there are no ingredients, then the recipe is invalid
        if(ingredients == null || ingredients.size() < 1) return null;
        
       //Check if we have all of our required tools
        if(requiredTool != null) {
            if(utensil == null || (!utensil.equals(requiredTool))) return null;
        }
        
      //Check if we have all of our required seasonings
        if(requiredSeasonings != null) {
            if(seasonings == null || seasonings.size() < 1) return null;
            for(ICookingComponent s: requiredSeasonings) {
                if(!contains(seasonings, s)) return null;
            }
        }
        
        //Check if we have all of our required ingredients
        for(ICookingComponent i: requiredIngredients) {
            if(!contains(ingredients, i)) return null;
        }
        
        //Create a copy of the meal
        Meal meal = new Meal(result);

        //Add the bonuses for having the optional ingredients to this meal
        if(optionalIngredients != null) {
            for(Ingredient i: optionalIngredients) {
                if(ingredients.contains(i)) {
                    meal.addIngredient(i);
                }
            }
        }
        
        //Add the bonuses for having the optional seasonings to this meal
        if(optionalSeasonings != null) {
            if(seasonings != null && seasonings.size() > 0) {
                for(Seasoning s: optionalSeasonings) {
                    if(seasonings.contains(s)) {
                        meal.addSeasoning(s);
                    }
                }
            }
        }
        
        return meal;
    }

    public Meal getBestMeal() {        
        Meal meal = new Meal(result);

        if(optionalIngredients != null) {
            for(Ingredient i: optionalIngredients) {
                meal.addIngredient(i);
            }
        }
        
        //Add the bonuses for having the optional seasonings to this meal
        if(optionalSeasonings != null) {
            for(Seasoning s: optionalSeasonings) {
                meal.addSeasoning(s);
            }
        }
        
        return meal;
    }
}
