package joshie.harvest.crops.handlers;

import com.google.common.collect.ImmutableList;
import joshie.harvest.api.crops.IStateHandler;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.crops.blocks.BlockHFCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.ArrayList;
import java.util.List;

public class StateHandlerNull implements IStateHandler {
    private static ImmutableList<IBlockState> stateContainer;
    protected IBlockState state;

    public StateHandlerNull() {
        state = HFCrops.CROPS.getDefaultState();
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
        return BlockHFCrops.CROP_AABB;
    }

    @Override
    public IBlockState getState(PlantSection section, int stage, boolean withered) {
        return state;
    }
}