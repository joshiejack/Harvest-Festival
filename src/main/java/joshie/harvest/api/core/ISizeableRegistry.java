package joshie.harvest.api.core;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/** This is for registering sizeable types **/
public interface ISizeableRegistry {
    /** Returns the size of this item
     *  @param stack the item to check */
    Size getSize(@Nonnull ItemStack stack);

    /** Register an item as a sizeable, of a specific size
     *  @param stack the item to register
     *  @param size the size to register it as**/
    void registerStackAsSize(@Nonnull ItemStack stack, Size size);
}