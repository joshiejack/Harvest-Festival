package joshie.harvestmoon.api.core;

import net.minecraft.item.ItemStack;

public interface ISizedProvider {
    public ISizeable getSizeable(ItemStack stack);
}
