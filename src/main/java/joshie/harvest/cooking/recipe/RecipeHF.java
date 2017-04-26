package joshie.harvest.cooking.recipe;

import joshie.harvest.api.cooking.IngredientStack;
import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.cooking.HFCooking;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class RecipeHF extends Recipe {
    public RecipeHF(ResourceLocation resource, Utensil utensil, int hunger, float saturation, IngredientStack... required) {
        super(resource, utensil, required);
        setFoodStats(hunger, saturation);
    }

    @Override
    @Nonnull
    public ItemStack getStack() {
        return HFCooking.MEAL.getStackFromRecipe(this);
    }
}
