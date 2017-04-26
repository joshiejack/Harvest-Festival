package joshie.harvest.api.crops;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class StateHandlerBlock implements IStateHandler<Crop> {
    @SuppressWarnings("WeakerAccess")
    protected static final AxisAlignedBB CROP_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected final int[] values;
    protected final Block block;

    public StateHandlerBlock(Block block, int... values) {
        this.block = block;
        this.values = values;
    }

    @Override
    public ImmutableList<IBlockState> getValidStates() {
        return block.getBlockState().getValidStates();
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockAccess world, BlockPos pos, PlantSection section, Crop crop, int stage, boolean withered) {
        return CROP_AABB;
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getState(IBlockAccess world, BlockPos pos, PlantSection section, Crop crop, int stage, boolean withered) {
        for (int i = 0; i < values.length; i++) {
            if (stage <= values[i]) return block.getStateFromMeta(i);
        }

        return block.getStateFromMeta(values.length - 1);
    }
}