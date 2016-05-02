package joshie.harvest.crops.handlers;

import com.google.common.collect.ImmutableList;
import joshie.harvest.api.crops.IStateHandler;
import joshie.harvest.blocks.BlockCrop;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;

public abstract class StateHandlerVanilla implements IStateHandler {
    protected Block block;

    public StateHandlerVanilla(Block block) {
        this.block = block;
    }

    @Override
    public ImmutableList<IBlockState> getValidStates() {
        return null;
    }

    @Override
    public AxisAlignedBB getBoundingBox(PlantSection section, int stage, boolean withered) {
        return BlockCrop.CROP_AABB;
    }
}