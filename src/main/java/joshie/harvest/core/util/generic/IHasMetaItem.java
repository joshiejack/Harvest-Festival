package joshie.harvest.core.util.generic;

import net.minecraft.item.ItemStack;

public interface IHasMetaItem {
    int getMetaCount();
    String getName(ItemStack stack);
}