package joshie.harvest.api.core;

import net.minecraft.item.ItemStack;

public interface ICreativeSorted {
    int getSortValue(ItemStack stack);
}