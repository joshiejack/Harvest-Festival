package joshie.harvest.api.crops;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;

public interface IStateHandler {
    public AxisAlignedBB getBoundingBox(PlantSection section, int stage, boolean withered);

    /** Return the correct block state for this stage **/
    public IBlockState getState(PlantSection section, int stage, boolean withered);

    public static enum PlantSection {
        TOP, BOTTOM;
    }
}