package joshie.harvest.crops.handlers;

import joshie.harvest.api.crops.IStateHandler;
import joshie.harvest.blocks.BlockCrop;
import joshie.harvest.blocks.HFBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;

public class StateHandlerNull implements IStateHandler {
    protected IBlockState state;

    public StateHandlerNull() {
        state = HFBlocks.CROPS.getDefaultState();
    }

    @Override
    public AxisAlignedBB getBoundingBox(PlantSection section, int stage, boolean withered) {
        return BlockCrop.CROP_AABB;
    }

    @Override
    public IBlockState getState(PlantSection section, int stage, boolean withered) {
        return state;
    }
}