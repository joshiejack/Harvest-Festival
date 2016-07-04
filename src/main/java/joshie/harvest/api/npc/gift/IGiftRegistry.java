package joshie.harvest.api.npc.gift;

import net.minecraft.item.ItemStack;

public interface IGiftRegistry {
    /** Returns whether this stack is of a specific GiftCategory
     *  @param category     the category to check
     *  @param stack         the item to check
     *  @return true if the stack is of the category */
    boolean isGiftType(GiftCategory category, ItemStack stack);
}
