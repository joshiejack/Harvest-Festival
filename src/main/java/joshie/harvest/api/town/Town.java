package joshie.harvest.api.town;

import joshie.harvest.api.buildings.BuildingLocation;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public interface Town {
    /** Returns the block position location for this building location
     *  @param location     the building location
     *  @return the position**/
    BlockPos getCoordinatesFor(@Nonnull BuildingLocation location);
}
