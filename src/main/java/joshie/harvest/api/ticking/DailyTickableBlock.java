package joshie.harvest.api.ticking;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class DailyTickableBlock {
    private final Phases[] phases;

    public DailyTickableBlock(Phases... phases) {
        this.phases = phases;
    }

    /** The phase that will tick will occur in **/
    public Phases[] getPhases() {
        return phases;
    }

    /** Validates this tickable block, if it's invalid then we will remove it from the list
     * @param world     the world object
     * @param pos       the world position
     * @param state     the block state **/
    public abstract boolean isStateCorrect(World world, BlockPos pos, IBlockState state);

    /** Will be called if this block is loaded, to perform the daily tick
     *  @param world    the world object
     *  @param pos      the position
     *  @param state    the blocks state */
    public abstract void newDay(World world, BlockPos pos, IBlockState state);

    /** PRIORITY = occurs before tickable blocks tick
     *  PRE = occurs before animals, and town ticks
     *  MAIN = occurs after the animal and town ticks
     *  POST = occurs after main*/
    public enum Phases {
        PRE, MAIN, POST
    }
}
