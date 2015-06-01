package joshie.harvest.api.core;

import net.minecraft.item.ItemStack;

/** Items that implement this interface are levelable
 *  And will display their current progress. **/
public interface ILevelable {

    /** Returns the rating of this item. Values returned should
     *  between 1-100% 
     *  
     *  @param  stack   the item
     *  @return         the percentage complete **/
    int getLevel(ItemStack stack);
}
