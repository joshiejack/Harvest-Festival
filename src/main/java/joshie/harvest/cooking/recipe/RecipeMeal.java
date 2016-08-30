package joshie.harvest.cooking.recipe;

import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.api.cooking.RecipeHandler;
import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.cooking.CookingAPI;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RecipeMeal implements RecipeHandler {
    @Override
    public ItemStack getResult(Utensil utensil, List<ItemStack> stacks, List<Ingredient> ingredients) {
        for (MealImpl recipe : CookingAPI.REGISTRY) {
            ItemStack meal = recipe.getResult(utensil, stacks, ingredients);
            if (meal != null) {
                return meal;
            }
        }

        return null;
    }
}
