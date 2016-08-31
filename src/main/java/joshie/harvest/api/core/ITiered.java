package joshie.harvest.api.core;

import net.minecraft.item.ItemStack;

/** Items that implement this interface are associated with a certain
 *  ToolTier, this can affect various things **/
public interface ITiered {
    /** Returns the rating of this item. Values returned should
     *  between 1-100%
     *
     *  @param  stack   the item
     *  @return         the percentage complete **/
    double getLevel(ItemStack stack);

    /** Increases the tools level
     * @param stack the item **/
    void levelTool(ItemStack stack);

    /** Returns the tier of this item
     *  
     *  @param  stack   the item
     *  @return         the tier **/
    ToolTier getTier(ItemStack stack);

    enum ToolTier {
        BASIC, COPPER, SILVER, GOLD, MYSTRIL, CURSED, BLESSED, MYTHIC;

        public boolean isGreaterThanOrEqualTo(ToolTier tier) {
            return this.ordinal() >= tier.ordinal() || ((tier == CURSED || tier == BLESSED) && (this == CURSED || this == BLESSED));
        }
    }
}