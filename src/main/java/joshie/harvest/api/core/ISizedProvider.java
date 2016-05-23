package joshie.harvest.api.core;

import net.minecraft.item.ItemStack;

public interface ISizedProvider {
    ISizeable getSizeable(ItemStack stack);
}