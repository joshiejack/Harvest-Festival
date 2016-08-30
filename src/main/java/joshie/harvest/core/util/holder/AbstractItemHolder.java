package joshie.harvest.core.util.holder;

import joshie.harvest.api.crops.ICropProvider;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.item.ItemSizeable;
import joshie.harvest.core.helpers.generic.StackHelper;
import joshie.harvest.crops.Crop;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractItemHolder extends AbstractHolder {
    private List<ItemStack> matchingStacks;

    public static AbstractItemHolder getStack(ItemStack stack) {
        if (stack.getItem() instanceof ICropProvider) return CropHolder.of((Crop)((ICropProvider)stack.getItem()).getCrop(stack));
        else if (stack.getItem() instanceof ItemSizeable) return SizeableHolder.of(HFCore.SIZEABLE.getObjectFromStack(stack));
        else if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE) return ItemHolder.of(stack.getItem());
        else return ItemStackHolder.of(stack);
    }

    @SideOnly(Side.CLIENT)
    public List<ItemStack> getMatchingStacks() {
        if (matchingStacks != null && matchingStacks.size() > 0) return matchingStacks;
        else {
            matchingStacks = new ArrayList<>();
            for (ItemStack stack: StackHelper.getAllStacks()) {
                if (matches(stack)) matchingStacks.add(stack.copy());
            }

            return matchingStacks;
        }
    }

    public abstract boolean matches(ItemStack stack);
    public abstract NBTTagCompound writeToNBT(NBTTagCompound tag);
}
