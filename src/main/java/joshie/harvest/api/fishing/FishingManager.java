package joshie.harvest.api.fishing;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface FishingManager {
    /** Register as valid for the fishing collection
     *  @param object can be an Item, a Block, an ItemStack, an Ore or a Mod**/
    void registerForFishingCollection(Object object);

    /** Register an item as bait, this means it can be used
     *  to catch things in the lobster trap
     *  @param stack    the item to add*/
    void registerBait(@Nonnull ItemStack stack);

    /** Register an item as breedable in the hatchery
     * @param stack the item that is able to be multiplied
     * @param days the number of days this item takes to multiply */
    void registerAsBreedable(@Nonnull ItemStack stack, int days);
}