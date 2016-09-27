package joshie.harvest.api.crops;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IStateHandler {
    /** Returns a list of all valid states **/
    ImmutableList<IBlockState> getValidStates();

    /** Returns the bounding box for this crop **/
    AxisAlignedBB getBoundingBox(IBlockAccess world, BlockPos pos, PlantSection section, int stage, boolean withered);

    /** Return the correct block state for this stage **/
    IBlockState getState(IBlockAccess world, BlockPos pos, PlantSection section, int stage, boolean withered);

    enum PlantSection {
        TOP, BOTTOM
    }

}