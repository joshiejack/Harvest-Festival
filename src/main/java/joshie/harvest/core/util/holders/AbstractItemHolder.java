package joshie.harvest.core.util.holders;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public abstract class AbstractItemHolder extends AbstractHolder {
    protected List<ItemStack> matchingStacks;
    public abstract List<ItemStack> getMatchingStacks();
    public abstract boolean matches(ItemStack stack);
    public abstract NBTTagCompound writeToNBT(NBTTagCompound tag);
}
