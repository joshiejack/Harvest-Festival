package joshie.harvest.plugins.crafttweaker.base;

import minetweaker.IUndoableAction;
import minetweaker.api.item.IIngredient;
import minetweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;

public abstract class BaseUndoable implements IUndoableAction {
    private boolean applied;

    @Override
    public boolean canUndo() {
        return true;
    }

    public abstract String getDescription();

    @Override
    public String describe() {
        return "[Harvest Festival] " + getDescription();
    }

    @Override
    public String describeUndo() {
        return "";
    }

    @Override
    public Object getOverrideKey() {
        return null;
    }

    //Helpers
    public static ItemStack asStack(IIngredient ingredient) {
        return ingredient.getInternal() instanceof ItemStack ? (ItemStack) ingredient.getInternal() : null;
    }

    public static String asOre(IIngredient ingredient) {
        return ingredient.getInternal() instanceof IOreDictEntry ? ((IOreDictEntry) ingredient).getName() : null;
    }
}
