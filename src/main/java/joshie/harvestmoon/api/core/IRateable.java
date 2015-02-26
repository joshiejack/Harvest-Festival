package joshie.harvestmoon.api.core;

import net.minecraft.item.ItemStack;

/** Items that implement this interface have an inherent
 *  quality to them. And will display their star rating value. **/
public interface IRateable {
    
    /** Returns the rating of this item. Values returned should
     *  between 0-9 (with 0 being 0.5 stars, and 9 being 5 stars)
     *  
     *  @param  stack   the item
     *  @return         the rating **/
    public int getRating(ItemStack stack);
}
