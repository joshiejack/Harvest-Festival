package joshie.harvest.api.ticking;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** These should only be called on serverside **/
public interface IDailyTickableRegistry {
    /** Use this to register a block that doesn't implement IDailyTickableBlock,
     *  as needing to have the handler called for it instead. */
    void registerDailyTickableBlock(Block block, IDailyTickableBlock daily);

    /** Call this when a tickable is ready,
     *  for tile entities, you can all this in validate
     * @param tickable the object */
    void addTickable(World world, IDailyTickable tickable);

    /** Call this to tick this block daily,
     *  Ideally call this when a block is added,
     *  also makes sure your block implements IDailyTickableBlock
     *  Otherwise this is a waste of time**/
    void addTickable(World world, BlockPos pos, IDailyTickableBlock type);

    /** Call this when a tickable is removed,
     *  for tile entities, you can all this in invalidate
     * @param tickable the object */
    void removeTickable(World world, IDailyTickable tickable);

    /** Call this to remove a daily block tick*/
    void removeTickable(World world, BlockPos pos);

    /** Returns the tickable based on the block **/
    IDailyTickableBlock getTickableFromBlock(Block block);
}
