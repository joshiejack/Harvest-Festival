package joshie.harvest.api.town;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITownHelper {
    /** Returns the town that's closest to this block position
     * @param world    the world
     * @param pos       the position**/
    Town getTownForBlockPos(World world, BlockPos pos);

    /** Returns the town this player is currently considered to be in
     * @param entity    the player **/
    Town getTownForEntity(Entity entity);
}