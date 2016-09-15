package joshie.harvest.cooking.recipe;

import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.api.cooking.Meal;
import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.cooking.CookingAPI;
import joshie.harvest.cooking.HFCooking;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;

public class MealImpl extends IForgeRegistryEntry.Impl<MealImpl> implements Meal {
    private final String name;
    public final MealBuilder result;
    private final Ingredient[] requiredIngredients;
    private ItemStack alt = null;

    //Optional Extras
    private final Utensil requiredTool;
    private Ingredient[] optionalIngredients;

    public MealImpl(String unlocalised, Utensil utensil, Ingredient[] ingredients, MealBuilder result) {
        this.name = unlocalised;
        this.requiredTool = utensil;
        this.requiredIngredients = ingredients;
        this.result = result;
    }

    @Override
    public MealImpl setOptionalIngredients(Ingredient... ingredients) {
        this.optionalIngredients = ingredients;
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

    @Override
    public MealImpl setExhaustion(float value) {
        this.result.setExhaustion(value);
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

    private Ingredient getIngredientIfInList(List<Ingredient> ingredients, Ingredient required) {
        //Now we should loop through all the ingredient passed in
        for (Ingredient passed : ingredients) {
            if (required.isEqual(passed))
                return passed; //If we found this ingredient in the list, then we can return it as true;
        }

        return null; //We did not find the item, therefore we return false
    }

    public ItemStack getResult(Utensil utensil, List<ItemStack> stacks, List<Ingredient> ingredients) {
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
            if (getIngredientIfInList(ingredients, required) == null) return null;
        }

        /** Final step is to build the meal **/
        MealBuilder meal = new MealBuilder(result, stacks);
        if (optionalIngredients != null) { //Loop through optional ingredients
            for (Ingredient optional : optionalIngredients) {
                Ingredient ingredient =  getIngredientIfInList(ingredients, optional);
                if (ingredient != null) { //If the optional ingredients list has this item
                    meal.addIngredient(ingredient);
                }
            }
        }

        return cook(meal);
    }

    public MealBuilder getMeal() {
        return new MealBuilder(result, getIngredientsAsStacks(false)); //Clone the meal
    }

    private void addStack(List<ItemStack> list, Ingredient ingredient) {
        List<ItemStack> stack = CookingAPI.INSTANCE.getStacksForIngredient(ingredient);
        if (stack.size() > 0 && stack.get(0) != null) { //Add the first item that matches to the list
            list.add(stack.get(0));
        }
    }

    private List<ItemStack> getIngredientsAsStacks(boolean getBest) {
        if (getBest) {
            List<ItemStack> best = new ArrayList<>();
            for (Ingredient ingredient: requiredIngredients)
                addStack(best, ingredient);
            if (optionalIngredients != null) {
                for (Ingredient ingredient : optionalIngredients)
                    addStack(best, ingredient);
            }

            return best;
        } else {
            List<ItemStack> basic = new ArrayList<>();
            for (Ingredient ingredient: requiredIngredients)
                addStack(basic, ingredient);
            return basic;
        }
    }

    public MealBuilder getBestMeal() {
        MealBuilder meal = new MealBuilder(result, getIngredientsAsStacks(true));
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
