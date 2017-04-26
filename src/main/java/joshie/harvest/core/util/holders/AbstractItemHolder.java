package joshie.harvest.core.util.holders;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

public abstract class AbstractItemHolder extends AbstractHolder implements Comparable<AbstractItemHolder> {
    protected NonNullList<ItemStack> matchingStacks;
    public abstract NonNullList<ItemStack> getMatchingStacks();
    public abstract boolean matches(@Nonnull ItemStack stack);
    public abstract NBTTagCompound writeToNBT(NBTTagCompound tag);

    @Override
    public int compareTo(@Nonnull AbstractItemHolder o) {
        return toString().compareToIgnoreCase(o.toString());
    }
}
