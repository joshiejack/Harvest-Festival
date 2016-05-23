package joshie.harvest.api.core;

import net.minecraft.item.ItemStack;

public interface IShippingRegistry {
    /** Alternative if you don't want to implement IShippable **/
    void registerSellable(ItemStack stack, long value);

    /** Returns this items sell vlaue, if it's not sellable, it returns 0L **/
    long getSellValue(ItemStack stack);
}
