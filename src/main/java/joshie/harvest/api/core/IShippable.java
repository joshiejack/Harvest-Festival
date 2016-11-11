package joshie.harvest.api.core;

import net.minecraft.item.ItemStack;

@Deprecated //TODO: Remove in 0.7+
//Register with the api or add a long nbt tag called "SellValue"
public interface IShippable {
    long getSellValue(ItemStack stack);
}