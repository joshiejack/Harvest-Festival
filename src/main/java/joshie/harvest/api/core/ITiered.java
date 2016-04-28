package joshie.harvest.api.core;

import net.minecraft.item.ItemStack;

/** Items that implement this interface are associated with a certain
 *  ToolTier, this can affect various things **/
public interface ITiered {

    /** Returns the tier of this item
     *  
     *  @param  stack   the item
     *  @return         the tier **/
    public ToolTier getTier(ItemStack stack);

    public static enum ToolTier {
        BASIC, COPPER, SILVER, GOLD, MYSTRIL, CURSED, BLESSED, MYTHIC;
    }
}