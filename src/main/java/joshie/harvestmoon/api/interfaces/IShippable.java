package joshie.harvestmoon.api.interfaces;

import net.minecraft.item.ItemStack;

/** Items that implement this interface can be shipped **/
public interface IShippable {
    
    /** Returns the sell value of the ItemStack passed in. 
     *  This is called when you attempt to add an item to 
     *  the shipping crate.
     *  
     *  @param  stack   the item
     *  @return         the value the item is shipped for**/
    public long getSellValue(ItemStack stack);
}
