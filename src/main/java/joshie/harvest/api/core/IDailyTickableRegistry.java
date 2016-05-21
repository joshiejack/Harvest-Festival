package joshie.harvest.api.core;

import net.minecraft.world.World;

public interface IDailyTickableRegistry {
    /** Call this when a tickable is ready,
     *  for tile entities, you can all this in validate
     * @param tickable the object */
    void addTickable(World world, IDailyTickable tickable);

    /** Call this when a tickable is removed,
     *  for tile entities, you can all this in invalidate
     * @param tickable the object */
    void removeTickable(World world, IDailyTickable tickable);
}
