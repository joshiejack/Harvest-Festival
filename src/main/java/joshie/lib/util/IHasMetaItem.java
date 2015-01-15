package joshie.lib.util;

import net.minecraft.item.ItemStack;

public interface IHasMetaItem {
    public int getMetaCount();
    public String getName(ItemStack stack);
}
