package joshie.harvestmoon.util;

import net.minecraft.item.ItemStack;

public interface IShippable {
    public static final double SELL_QUALITY_MODIFIER = 0.01825D;
    
    public int getSellValue(ItemStack stack);
}
