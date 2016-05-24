package joshie.harvest.crops.handlers;

import com.google.common.collect.ImmutableList;
import gnu.trove.map.TIntObjectMap;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.crops.IStateHandler;
import joshie.harvest.crops.blocks.BlockHFCrops;
import joshie.harvest.blocks.HFBlocks;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;

public class StateHandlerDefault implements IStateHandler {
    protected BlockStateContainer stateContainer;
    protected PropertyInteger stages;
    protected IBlockState defaultState;

    protected TIntObjectMap<IBlockState> statesMap;
    protected final int maximum;

    public StateHandlerDefault(int maximum) {
        this.maximum = maximum;
        this.stages = PropertyInteger.create("stage", 1, maximum);
        this.stateContainer = new BlockStateContainer(HFBlocks.CROPS, stages);
        this.defaultState = stateContainer.getBaseState().withProperty(stages, Integer.valueOf(1));
    }

    public StateHandlerDefault(ICrop crop) {
        this(crop.getStages());
    }

    @Override
    public ImmutableList<IBlockState> getValidStates() {
        return stateContainer.getValidStates();
    }

    @Override
    public AxisAlignedBB getBoundingBox(PlantSection section, int stage, boolean withered) {
        return BlockHFCrops.CROP_AABB;
    }

    @Override
    public IBlockState getState(PlantSection section, int stage, boolean withered) {
        return getState(stage);
    }

    protected IBlockState getState(int stage) {
        return defaultState.withProperty(stages, Integer.valueOf(stage));
    }
}