package joshie.harvest.api.cooking;

import net.minecraft.item.ItemStack;

public interface IKnife {
    /** Return true if this item is a knife
      * @param stack    the item
     * @return */
    boolean isKnife(ItemStack stack);
}
