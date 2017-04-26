package joshie.harvest.api.core;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;


//This is implemented on items that provide different sizes
public interface ISizedProvider<E> {
    /** Return the size of the product
     *  @param stack    the stack **/
    Size getSize(@Nonnull ItemStack stack);

    /** Return the object this stack represents
     * @param stack the stack  */
    E getObject(@Nonnull ItemStack stack);

    /** Return this object loaded from a string **/
    E getObjectFromString(String object);

    /** Return a stack for this item of this size **/
    @Nonnull
    ItemStack getStackOfSize(E e, Size size, int amount);
}