package joshie.harvest.api.core;

import net.minecraft.item.ItemStack;


//This is implemented on items that provide different sizes
public interface ISizedProvider<E> {
    /** Return the size of the product
     *  @param stack    the stack **/
    Size getSize(ItemStack stack);

    /** Return the object this stack represents
     * @param stack  */
    E getObject(ItemStack stack);

    /** Return this object loaded from a string **/
    E getObjectFromString(String object);

    /** Return a stack for this item of this size **/
    ItemStack getStackOfSize(E e, Size size, int amount);
}