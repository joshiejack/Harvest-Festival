package joshie.harvest.core.util.generic;

import net.minecraft.item.ItemStack;

public interface IHasMetaItem {
    public int getMetaCount();
    public String getName(ItemStack stack);
}