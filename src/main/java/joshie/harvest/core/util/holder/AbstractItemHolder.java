package joshie.harvest.core.util.holder;

import joshie.harvest.api.crops.ICropProvider;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.item.ItemSizeable;
import joshie.harvest.crops.Crop;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public abstract class AbstractItemHolder extends AbstractHolder {
    protected List<ItemStack> matchingStacks;

    public static AbstractItemHolder getStack(ItemStack stack) {
        if (stack.getItem() instanceof ICropProvider) return CropHolder.of((Crop)((ICropProvider)stack.getItem()).getCrop(stack));
        else if (stack.getItem() instanceof ItemSizeable) return SizeableHolder.of(HFCore.SIZEABLE.getObjectFromStack(stack));
        else if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE) return ItemHolder.of(stack.getItem());
        else return ItemStackHolder.of(stack);
    }

    public abstract List<ItemStack> getMatchingStacks();
    public abstract boolean matches(ItemStack stack);
    public abstract NBTTagCompound writeToNBT(NBTTagCompound tag);
}
