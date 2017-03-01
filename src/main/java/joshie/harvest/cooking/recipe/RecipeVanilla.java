package joshie.harvest.cooking.recipe;

import joshie.harvest.api.cooking.IngredientStack;
import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.api.cooking.Utensil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RecipeVanilla extends Recipe {
    private final ItemStack stack;

    public RecipeVanilla(ResourceLocation resource, ItemStack result, Utensil utensil, IngredientStack... required) {
        super(resource, utensil, required);
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
