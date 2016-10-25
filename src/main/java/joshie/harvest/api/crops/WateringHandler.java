package joshie.harvest.api.crops;

import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WateringHandler {
    private static final IBlockState WET_SOIL = Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, 7);

    /** Called to check if this state is considered wet
     *  @param   world   the world object
     * @param    pos    the position being watered
     * @param    state  the current state
     *  @return  true if the state is wet**/
    public boolean isWet(World world, BlockPos pos, IBlockState state) {
        return state == WET_SOIL;
    }

    /** Called to check if this handler will work for this state
     *
     * @param   world   the world object
     * @param    pos    the position being watered
     * @param    state  the current state
     * @return  true if this handler, will handle this states checks*/
    public boolean handlesState(World world, BlockPos pos, IBlockState state) {
        return state.getBlock() == Blocks.FARMLAND;
    }

    /** Called to water this land, only will go here if isWet returns false
     *
     * @param   world   the world object
     * @param    pos    the position being watered
     * @param    state  the current state
     * @return   true if we we watered the crop **/
    public boolean water(World world, BlockPos pos, IBlockState state) {
        world.setBlockState(pos, WET_SOIL);
        return true;
    }
}
