package joshie.harvest.api.core;

import joshie.harvest.api.core.ISizeable.Size;
import net.minecraft.item.ItemStack;


//This is implemented on items that provide different sizes
public interface ISizedProvider {
    /** Return the size of the product **/
    Size getSize(ItemStack stack);
}