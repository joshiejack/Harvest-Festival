package joshie.harvest.api.fishing;

import net.minecraft.item.ItemStack;

public interface FishingManager {
    /** Register an item as bait, this means it can be used
     *  to catch things in the lobster trap
     *  @param stack    the item to add*/
    void registerBait(ItemStack stack);

    /** Register an item as breedable in the hatchery
     * @param stack the item that is able to be multiplied
     * @param days the number of days this item takes to multiply */
    void registerAsBreedable(ItemStack stack, int days);
}
