package joshie.harvestmoon.api;

import net.minecraft.item.ItemStack;

public interface IShippable {
    public static final double SELL_QUALITY_MODIFIER = 0.01825D;
   
    public long getSellValue(ItemStack stack);
}
