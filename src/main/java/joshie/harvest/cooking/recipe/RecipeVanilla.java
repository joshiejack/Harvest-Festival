package joshie.harvest.cooking.recipe;

import joshie.harvest.api.cooking.IngredientStack;
import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.api.cooking.Utensil;
import net.minecraft.item.ItemStack;

public class RecipeVanilla extends Recipe {
    private final ItemStack stack;

    public RecipeVanilla(ItemStack result, Utensil utensil, IngredientStack... required) {
        super(utensil, required);
        this.stack = result;
    }

    @Override
    public String getDisplayName() {
        return stack.getDisplayName();
    }

    @Override
    public boolean supportsNBTData() {
        return false;
    }

    @Override
    public ItemStack getStack() {
        return stack;
    }
}
