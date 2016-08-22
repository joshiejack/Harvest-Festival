package joshie.harvest.cooking;

import joshie.harvest.api.cooking.ICookingIngredient;
import joshie.harvest.api.cooking.IMeal;
import joshie.harvest.api.cooking.IMealRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

import java.util.HashSet;
import java.util.Set;

public class Recipe extends net.minecraftforge.fml.common.registry.IForgeRegistryEntry.Impl<Recipe> implements IMealRecipe {
    private final String name;
    public final IMeal result;
    private final ICookingIngredient[] requiredIngredients;

    //Optional Extras
    private Utensil requiredTool;
    private ICookingIngredient[] optionalIngredients;

    public Recipe(String unlocalised, ICookingIngredient[] ingredients, IMeal result) {
        this.name = unlocalised;
        this.requiredIngredients = ingredients;
        this.result = result;
    }

    @Override
    public String getDisplayName() {
        return I18n.translateToLocal(name);
    }

    public Utensil getRequiredTool() {
        return requiredTool;
    }

    @Override
    public IMealRecipe setOptionalIngredients(ICookingIngredient... ingredients) {
        this.optionalIngredients = ingredients;
        return this;
    }

    @Override
    public IMealRecipe setRequiredTool(Utensil tool) {
        this.requiredTool = tool;
        return this;
    }

    @Override
    public IMealRecipe setIsDrink() {
        this.result.setIsDrink();
        return this;
    }

    @Override
    public IMealRecipe setAlternativeTexture(ItemStack stack) {
        this.result.setAlternativeItem(stack);
        return this;
    }

    private boolean recipeHasThisIngredient(ICookingIngredient ingredient) {
        //First we check if the Required Ingredients have this ingredient in them
        for (ICookingIngredient i : requiredIngredients) {
            if (i.isEqual(ingredient)) {
                return true;
            }
        }

        //Second we check if the Optional Ingredients have this ingredient in them
        if (optionalIngredients != null) {
            for (ICookingIngredient i : optionalIngredients) {
                if (i.isEqual(ingredient)) return true;
            }
        }

        //Since neither the required ingredients or optional ones, had this recipe. Then the recipe should fail.
        return false;
    }

    private boolean ingredientListContains(Set<ICookingIngredient> ingredients, ICookingIngredient required) {
        //Now we should loop through all the ingredient passed in
        for (ICookingIngredient passed : ingredients) {
            if (required.isEqual(passed))
                return true; //If we found this ingredient in the list, then we can return it as true;
        }

        return false; //We did not find the item, therefore we return false
    }

    @Override
    public IMeal getMeal(Utensil utensil, HashSet<ICookingIngredient> ingredients) {
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

        /** Final step is to build the meal **/
        IMeal meal = new Meal(result);
        if (optionalIngredients != null) { //Loop through optional ingredients
            for (ICookingIngredient optional : optionalIngredients) {
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
            for (ICookingIngredient i : optionalIngredients) {
                meal.addIngredient(i);
            }
        }

        return meal;
    }

    @Override
    public ItemStack cook(IMeal meal) {
        return HFCooking.MEAL.cook(this, meal);
    }
}
