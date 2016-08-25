package joshie.harvest.cooking.recipe;

import joshie.harvest.api.cooking.ICookingIngredient;
import joshie.harvest.api.cooking.IMeal;
import joshie.harvest.api.cooking.ISpecialRecipeHandler;
import joshie.harvest.cooking.FoodRegistry;
import joshie.harvest.cooking.Utensil;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RecipeMeal implements ISpecialRecipeHandler {
    @Override
    public ItemStack getResult(Utensil utensil, List<ItemStack> stacks, List<ICookingIngredient> ingredients) {
        for (Recipe recipe : FoodRegistry.REGISTRY) {
            IMeal meal = recipe.getMeal(utensil, ingredients);
            if (meal != null) {
                return recipe.cook(meal);
            }
        }

        return null;
    }
}
