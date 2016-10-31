package joshie.harvest.cooking.recipe;

import joshie.harvest.api.cooking.IngredientStack;
import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.cooking.HFCooking;
import net.minecraft.item.ItemStack;

public class RecipeHF extends Recipe {
    public RecipeHF(Utensil utensil, int hunger, float saturation, IngredientStack... required) {
        super(utensil, required);
        setFoodStats(hunger, saturation);
    }

    @Override
    public ItemStack getStack() {
        return new ItemStack(HFCooking.MEAL, 1, Recipe.REGISTRY.getValues().indexOf(this));
    }
}
