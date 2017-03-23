package joshie.harvest.api.npc.gift;

import net.minecraft.item.ItemStack;

public interface IGiftRegistry {
    /** Blacklist an item, this means it cannot be gifted
     * @param object the objects
     *
     *      Acceptable values are
     *      @see net.minecraft.block.Block
     *      @see net.minecraft.item.Item
     *      @see net.minecraft.item.ItemStack
     *      @see joshie.harvest.api.core.Mod
     *      @see joshie.harvest.api.core.Ore
     **/
    void addToBlacklist(Object... object);

    /** Assign a block, item, stack, or mod
     *      Acceptable values are
     *      @see net.minecraft.block.Block
     *      @see net.minecraft.item.Item
     *      @see net.minecraft.item.ItemStack
     *      @see joshie.harvest.api.core.Mod
     *      @see joshie.harvest.api.core.Ore
     **/
    void setCategory(Object object, GiftCategory categories);

    /** Returns whether this stack is of a specific GiftCategory
     *  @param categories     the categories to check
     *  @param stack         the item to check
     *  @return true if the stack is of the category */
    boolean isGiftType(ItemStack stack, GiftCategory categories);
}
