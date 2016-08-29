package joshie.harvest.cooking.recipe;

import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.api.cooking.Meal;
import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.cooking.CookingAPI;
import joshie.harvest.cooking.HFCooking;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

import java.util.List;

public class MealImpl extends IForgeRegistryEntry.Impl<MealImpl> implements Meal {
    private final String name;
    public final MealBuilder result;
    private final Ingredient[] requiredIngredients;
    private ItemStack alt = null;

    //Optional Extras
    private Utensil requiredTool;
    private Ingredient[] optionalIngredients;

    public MealImpl(String unlocalised, Ingredient[] ingredients, MealBuilder result) {
        this.name = unlocalised;
        this.requiredIngredients = ingredients;
        this.result = result;
    }

    @Override
    public MealImpl setOptionalIngredients(Ingredient... ingredients) {
        this.optionalIngredients = ingredients;
        return this;
    }

    @Override
    public MealImpl setRequiredTool(Utensil tool) {
        this.requiredTool = tool;
        return this;
    }

    @Override
    public MealImpl setIsDrink() {
        this.result.setIsDrink();
        return this;
    }

    @Override
    public MealImpl setAlternativeTexture(ItemStack stack) {
        this.alt = stack;
        return this;
    }

    @SuppressWarnings("deprecation")
    public String getDisplayName() {
        return I18n.translateToLocal(name);
    }

    public ItemStack getAlternativeItem() {
        return alt;
    }

    public Utensil getRequiredTool() {
        return requiredTool;
    }

    public Ingredient[] getRequiredIngredients() {
        return requiredIngredients;
    }

    private boolean recipeHasThisIngredient(Ingredient ingredient) {
        //First we check if the Required Ingredients have this ingredient in them
        for (Ingredient i : requiredIngredients) {
            if (i.isEqual(ingredient)) {
                return true;
            }
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
            if (required.isEqual(passed))
                return true; //If we found this ingredient in the list, then we can return it as true;
        }

        return false; //We did not find the item, therefore we return false
    }

    public ItemStack getResult(Utensil utensil, List<Ingredient> ingredients) {
        if (ingredients == null || ingredients.size() < 1 || utensil != requiredTool)
            return null; //If we have no utensils, or not enough recipes remove them all

        /** Step one.
         *  Validate that all supplied Ingredients are Allowed in this Meal.*/
        for (Ingredient ingredient : ingredients) { //Loop through all the ingredients to CHECK if the recipe allow this type of food in to it
            if (!recipeHasThisIngredient(ingredient))
                return null; //If the recipe DOES not contain this ingredient then we should return null.
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
        MealBuilder meal = new MealBuilder(result);
        if (optionalIngredients != null) { //Loop through optional ingredients
            for (Ingredient optional : optionalIngredients) {
                if (ingredientListContains(ingredients, optional)) { //If the optional ingredients list has this item
                    meal.addIngredient(optional);
                }
            }
        }

        return cook(meal);
    }

    public MealBuilder getMeal() {
        return new MealBuilder(result); //Clone the meal
    }

    public MealBuilder getBestMeal() {
        MealBuilder meal = new MealBuilder(result);
        if (optionalIngredients != null) {
            for (Ingredient i : optionalIngredients) {
                meal.addIngredient(i);
            }
        }

        return meal;
    }

    //Apply all the relevant information about this meal to the meal stack
    public ItemStack cook(MealBuilder meal) {
        return meal.cook(new ItemStack(HFCooking.MEAL, 1, CookingAPI.REGISTRY.getValues().indexOf(this)));
    }
}
