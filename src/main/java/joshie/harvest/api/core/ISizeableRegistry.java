package joshie.harvest.api.core;

import joshie.harvest.api.core.ISizeable.Size;
import net.minecraft.item.ItemStack;

/** This is for registering sizeable types **/
public interface ISizeableRegistry {
    /** Returns the size of this item
     *  @param stack the item to check */
    Size getSize(ItemStack stack);

    /** Register an item as a sizeable, of a specific size
     *  @param stack the item to register
     *  @param size the size to register it as**/
    void registerStackAsSize(ItemStack stack, Size size);
}
