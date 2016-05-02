package joshie.harvest.api.crops;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;

public interface IStateHandler {
    /** Returns a list of all valid states **/
    ImmutableList<IBlockState> getValidStates();

    /** Returns the bounding box for this crop **/
    AxisAlignedBB getBoundingBox(PlantSection section, int stage, boolean withered);

    /** Return the correct block state for this stage **/
    IBlockState getState(PlantSection section, int stage, boolean withered);

    public enum PlantSection {
        TOP, BOTTOM;
    }
}