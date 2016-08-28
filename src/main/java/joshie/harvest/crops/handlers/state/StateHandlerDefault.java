package joshie.harvest.crops.handlers.state;

import com.google.common.collect.ImmutableList;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.crops.IStateHandler;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.crops.block.BlockHFCrops;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;

public class StateHandlerDefault implements IStateHandler {
    protected BlockStateContainer stateContainer;
    protected PropertyInteger stages;
    protected IBlockState defaultState;

    protected final int maximum;

    public StateHandlerDefault(int maximum) {
        this.maximum = maximum;
        this.stages = PropertyInteger.create("stage", 1, maximum);
        this.stateContainer = new BlockStateContainer(HFCrops.CROPS, stages);
        this.defaultState = stateContainer.getBaseState().withProperty(stages, 1);
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
        return defaultState.withProperty(stages, stage);
    }
}