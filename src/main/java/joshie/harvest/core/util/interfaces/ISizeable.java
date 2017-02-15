package joshie.harvest.core.util.interfaces;

import joshie.harvest.api.core.Size;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface ISizeable {
    /** Shorthand for single stacks
     * @param item      the item
     * @param size      the size */
    default ItemStack getStack(Item item, Size size) {
        return getStackOfSize(item, size, 1);
    }

    /** Returns a stack of this size **/
    default ItemStack getStackOfSize(Item item, Size size, int stackSize) {
        return new ItemStack(item, stackSize, (getMeta() * 3) + size.ordinal());
    }

    /** Returns the golden version of this **/
    ItemStack getGoldenProduct();

    /** Return the metadata for this sizeable **/
    int getMeta();

    default long getSellValue(Size size) {
        return size == Size.SMALL ? getSmall() : size == Size.MEDIUM ? getMedium() : getLarge();
    }

    /** The sell values for various sizes **/
    long getSmall();
    long getMedium();
    long getLarge();
    long getGolden();
}
