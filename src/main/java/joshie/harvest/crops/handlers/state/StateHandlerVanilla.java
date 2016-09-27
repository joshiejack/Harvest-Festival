package joshie.harvest.crops.handlers.state;

import com.google.common.collect.ImmutableList;
import joshie.harvest.api.crops.IStateHandler;
import joshie.harvest.crops.block.BlockHFCrops;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public abstract class StateHandlerVanilla implements IStateHandler {
    protected final Block block;

    public StateHandlerVanilla(Block block) {
        this.block = block;
    }

    @Override
    public ImmutableList<IBlockState> getValidStates() {
        return null;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockAccess world, BlockPos pos, PlantSection section, int stage, boolean withered) {
        return BlockHFCrops.CROP_AABB;
    }
}