package joshie.harvest.api.crops;

import joshie.harvest.crops.CropRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WateringHandler {
    private static final int WET_SOIL = 7;
    private static final int DRYING_SOIL = 3;
    private static final int DRY_SOIL = 0;

    /** Called to check if this state is considered wet
     *  @param   world   the world object
     * @param    pos    the position being watered
     * @param    state  the current state
     *  @return  true if the state is wet**/
    public boolean isWet(World world, BlockPos pos, IBlockState state) {
        return state.getValue(BlockFarmland.MOISTURE) == WET_SOIL;
    }

    /** Called to check if this handler will work for this state
     *
     * @param   world   the world object
     * @param    pos    the position being watered
     * @param    state  the current state
     * @return  true if this handler, will handle this states checks*/
    public boolean handlesState(World world, BlockPos pos, IBlockState state) {
        return state.getBlock() instanceof BlockFarmland;
    }

    /** Called to water this land, only will go here if isWet returns false
     *
     * @param   world   the world object
     * @param    pos    the position being watered
     * @param    state  the current state
     * @return   true if we we watered the crop **/
    public IBlockState hydrate(World world, BlockPos pos, IBlockState state) {
        return state.withProperty(BlockFarmland.MOISTURE, WET_SOIL);
    }

    /** Called every morning, to dehydrate this soil
     *
     * @param   world   the world object
     * @param    pos    the position being watered
     * @param    state  the current state **/
    public void dehydrate(World world, BlockPos pos, IBlockState state) {
        int moisture = state.getValue(BlockFarmland.MOISTURE);
        if (moisture == WET_SOIL) world.setBlockState(pos, state.withProperty(BlockFarmland.MOISTURE, DRYING_SOIL), 2);
        else if (moisture == DRYING_SOIL) world.setBlockState(pos, state.withProperty(BlockFarmland.MOISTURE, DRY_SOIL), 2);
        else if (moisture == DRY_SOIL && hasNoCrops(world, pos)) {
            IBlockState dirt = CropRegistry.INSTANCE.farmlandToDirtMap.get(state);
            world.setBlockState(pos, dirt != null ? dirt : Blocks.DIRT.getDefaultState(), 2);
        }
    }

    /** Checks if there is anything above the block **/
    protected boolean hasNoCrops(World worldIn, BlockPos pos)  {
        Block block = worldIn.getBlockState(pos.up()).getBlock();
        return !(block instanceof net.minecraftforge.common.IPlantable);
    }
}
