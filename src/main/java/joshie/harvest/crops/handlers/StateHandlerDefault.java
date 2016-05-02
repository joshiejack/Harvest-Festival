package joshie.harvest.crops.handlers;

import joshie.harvest.api.crops.IStateHandler;
import joshie.harvest.blocks.BlockCrop;
import joshie.harvest.blocks.HFBlocks;
import joshie.harvest.crops.Crop;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;

public class StateHandlerDefault implements IStateHandler {
    protected BlockStateContainer stateContainer;
    protected PropertyInteger stages;
    protected IBlockState state;
    protected Crop crop;

    public StateHandlerDefault(Crop crop) {
        this.crop = crop;
        stages = PropertyInteger.create("stage", 1, crop.getStages());
        stateContainer = new BlockStateContainer(HFBlocks.CROPS, stages);
        state = stateContainer.getBaseState();
    }

    @Override
    public AxisAlignedBB getBoundingBox(PlantSection section, int stage, boolean withered) {
        return BlockCrop.CROP_AABB;
    }

    @Override
    public IBlockState getState(PlantSection section, int stage, boolean withered) {
        System.out.println("Called this to get the states for " + crop.getLocalizedName(false));
        return state.withProperty(stages, Integer.valueOf(stage));
    }
}