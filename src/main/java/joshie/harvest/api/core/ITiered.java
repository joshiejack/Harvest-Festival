package joshie.harvest.api.core;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/** Items that implement this interface are associated with a certain
 *  ToolTier, this can affect various things **/
public interface ITiered {
    /** Returns the rating of this item. Values returned should
     *  between 1-100%
     *
     *  @param  stack   the item
     *  @return         the percentage complete **/
    double getLevel(@Nonnull ItemStack stack);

    /** Increases the tools level
     * @param stack the item **/
    void levelTool(@Nonnull ItemStack stack);

    /** Sets the level
     * @param stack the stack
     * @param value the level**/
    boolean setLevel(@Nonnull ItemStack stack, double value);

    /** Returns the tier of this item
     *  
     *  @param  stack   the item
     *  @return         the tier **/
    ToolTier getTier(@Nonnull ItemStack stack);

    enum ToolTier {
        BASIC(0, 128), COPPER(1, 256), SILVER(2, 768), GOLD(3, 1152), MYSTRIL(4, 3456), CURSED(5, 6912), BLESSED(5, 6912), MYTHIC(6, 13824);

        private final int level;
        private final int maxDamage;

        ToolTier(int level, int maxDamage) {
            this.level = level;
            this.maxDamage = maxDamage;
        }

        public int getToolLevel() {
            return level;
        }

        public int getMaximumDamage() {
            return maxDamage;
        }

        public ToolTier getNext() {
            int next = level + 1;
            for (ToolTier tier : values()) {
                if (tier.level == next) {
                    return tier;
                }
            }
            return this;
        }

        public boolean isGreaterThanOrEqualTo(ToolTier tier) {
            return this.ordinal() >= tier.ordinal() || ((tier == CURSED || tier == BLESSED) && (this == CURSED || this == BLESSED));
        }
    }
}