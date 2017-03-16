package joshie.harvest.api.town;

import joshie.harvest.api.buildings.Building;
import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.npc.NPC;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Town {
    /** Returns the block position location for this building, based off the offsets
     * @param building the build
     * @param offset   the offset
     * @return the block position **/
    @Nullable
    BlockPos getCoordinatesFromOffset(Building building, BlockPos offset);

    /** Returns the block position location for this building location
     *  @param location     the building location
     *  @return the position**/
    @Nullable
    BlockPos getCoordinatesFor(@Nonnull BuildingLocation location);

    /** Returns true if this town has the building
     *  @param building     the building to check for
     *  @return true if the town has the building, false if it doesn't**/
    boolean hasBuilding(Building building);

    /** Returns true if this town has the npc
     *  @param npc      the npc to check for
     *  @return true if the town has the npc, false if it doesn't **/
    boolean hasNPC(NPC npc);

    /** Returns the number of buildings in this town **/
    int getBuildingCount();
}
