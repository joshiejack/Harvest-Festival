package joshie.harvest.core.util.holders;

import joshie.harvest.api.core.ISizedProvider;
import joshie.harvest.api.crops.ICropProvider;
import joshie.harvest.crops.Crop;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public abstract class AbstractItemHolder extends AbstractHolder {
    public static AbstractItemHolder getStack(ItemStack stack) {
        if (stack.getItem() instanceof ICropProvider) return CropHolder.of((Crop)((ICropProvider)stack.getItem()).getCrop(stack));
        else if (stack.getItem() instanceof ISizedProvider) return SizeableHolder.of(((ISizedProvider)stack.getItem()).getSizeable(stack));
        else if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE) return ItemHolder.of(stack.getItem());
        else return ItemStackHolder.of(stack);
    }

    public abstract boolean matches(ItemStack stack);
    public abstract NBTTagCompound writeToNBT(NBTTagCompound tag);
}
