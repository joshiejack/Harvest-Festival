package joshie.harvest.api.buildings;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/** This is the building registry, it gets populated with all the buildings during,
 *  the init stage, so register buildings before hand and only use the other methods,
 *  after registration has been completed */
public interface IBuildingRegistry {
    /** Returns the blueprint for this building
     * @param building the building you want
     * @return the stack as a blueprint */
    @Nonnull
    ItemStack getBlueprint(Building building);

    /** Returns the spawner for this building
     * @param building the building you want
     * @return the stack as a spawner **/
    @Nonnull
    ItemStack getSpawner(Building building);
}