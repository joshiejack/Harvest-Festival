package joshie.harvest.api.ticking;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** These should only be called on serverside **/
public interface IDailyTickableRegistry {
    /** This should be called whenever this block gets added to the world
     *  For tile entities, you can often just call this in validate
     *  @param world        the world object
     *  @param pos          the block position
     *  @param tickable     the tickable handler**/
    void addTickable(World world, BlockPos pos, DailyTickableBlock tickable);
}
