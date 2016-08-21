package joshie.harvest.api.npc.gift;

import net.minecraft.item.ItemStack;

public interface IGiftRegistry {
    /** Register a gift type
     *  @param stack    the itemstack, use OreDictionary.WILDCARD_VALUE if you want it to only match the item
     *  @param categories the categories to match**/
    void assignStack(ItemStack stack, GiftCategory... categories);

    /** Register a mod id
     *  @param mod    If you don't feel like having to register the categories for all your items, you can add a blanket category based on the mod
     *  @param categories the categories to match**/
    void assignMod(String mod, GiftCategory... categories);

    /** Returns whether this stack is of a specific GiftCategory
     *  @param categories     the categories to check
     *  @param stack         the item to check
     *  @return true if the stack is of the category */
    boolean isGiftType(ItemStack stack, GiftCategory... categories);
}
