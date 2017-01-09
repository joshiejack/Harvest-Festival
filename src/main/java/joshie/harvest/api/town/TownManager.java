package joshie.harvest.api.town;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public interface TownManager {
    /** Grabs the closest town to the entity and checks if buildings with
     *  the ids passed are built in the town
     * @param entity    the entity to check
     * @param buildings the buildings to check
     * @return true if the town has the buildings */
    boolean doesClosestTownHaveBuildings(Entity entity, ResourceLocation... buildings);
}
