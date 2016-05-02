package joshie.harvest.crops.handlers;

import com.google.common.collect.ImmutableList;
import joshie.harvest.api.crops.IStateHandler;
import joshie.harvest.blocks.BlockCrop;
import joshie.harvest.blocks.HFBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.ArrayList;
import java.util.List;

public class StateHandlerNull implements IStateHandler {
    private static ImmutableList<IBlockState> stateContainer;
    protected IBlockState state;

    public StateHandlerNull() {
        state = HFBlocks.CROPS.getDefaultState();
        List<IBlockState> list = new ArrayList<IBlockState>();
        list.add(state);
        stateContainer = ImmutableList.<IBlockState>copyOf(list);
    }

    @Override
    public ImmutableList<IBlockState> getValidStates() {
        return stateContainer;
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