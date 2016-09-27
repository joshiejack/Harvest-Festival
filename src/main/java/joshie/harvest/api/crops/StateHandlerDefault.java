package joshie.harvest.api.crops;

import com.google.common.collect.ImmutableList;
import joshie.harvest.api.HFApi;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class StateHandlerDefault implements IStateHandler {
    public static final AxisAlignedBB CROP_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected BlockStateContainer stateContainer;
    protected PropertyInteger stages;
    protected IBlockState defaultState;

    protected final int maximum;

    public StateHandlerDefault(int maximum) {
        this.maximum = maximum;
        this.stages = PropertyInteger.create("stage", 1, maximum);
        this.stateContainer = HFApi.crops.getStateContainer(stages);
        this.defaultState = stateContainer.getBaseState().withProperty(stages, 1);
    }

    public StateHandlerDefault(Crop crop) {
        this(crop.getStages());
    }

    @Override
    public ImmutableList<IBlockState> getValidStates() {
        return stateContainer.getValidStates();
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockAccess world, BlockPos pos, PlantSection section, int stage, boolean withered) {
        return CROP_AABB;
    }

    @Override
    public IBlockState getState(IBlockAccess world, BlockPos pos, PlantSection section, int stage, boolean withered) {
        return getState(stage);
    }

    protected IBlockState getState(int stage) {
        return defaultState.withProperty(stages, stage);
    }
}
