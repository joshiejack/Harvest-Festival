package joshie.harvest.api.core;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface IShippingRegistry {
    /** Alternative if you don't want to implement IShippable
     *  @param stack   the stack  use OreDictionary.WILCARD_VALUE if you want to apply to the item
     *  @param value   the value**/
    void registerSellable(@Nonnull ItemStack stack, long value);

    /**  Register a sellable via ore name
    *   @param ore    The ore name
    *  @param value   the value**/
    void registerSellable(Ore ore, long value);

    /** Returns this items sell vlaue, if it's not sellable, it returns 0L **/
    long getSellValue(@Nonnull ItemStack stack);
}