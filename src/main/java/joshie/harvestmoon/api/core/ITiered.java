package joshie.harvestmoon.api.core;

import joshie.harvestmoon.items.ItemBaseTool.ToolTier;
import net.minecraft.item.ItemStack;

/** Items that implement this interface are associated with a certain
 *  ToolTier, this can affect various things **/
public interface ITiered {

    /** Returns the tier of this item
     *  
     *  @param  stack   the item
     *  @return         the tier **/
    ToolTier getTier(ItemStack stack);
}
