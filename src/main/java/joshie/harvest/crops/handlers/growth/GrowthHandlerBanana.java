package joshie.harvest.crops.handlers.growth;

import joshie.harvest.crops.HFCrops;
import joshie.harvest.crops.block.BlockFruit.Fruit;
import joshie.harvest.crops.block.BlockLeavesTropical.LeavesTropical;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("unused")
public class GrowthHandlerBanana extends GrowthHandlerHFTree {
    public GrowthHandlerBanana() {
        super(Fruit.BANANA, 1);
    }

    @Override
    protected boolean isLeaves(IBlockState state) {
        return state.getBlock() == HFCrops.LEAVES_TROPICAL && HFCrops.LEAVES_TROPICAL.getEnumFromState(state) == LeavesTropical.BANANA;
    }

    @Override
    protected BlockPos getAdjustedPositionBasedOnRotation(BlockPos pos, Rotation rotation) {
        switch (rotation) {
            case NONE:
                return pos.west(4).north(4);
            case CLOCKWISE_90:
                return pos.north(4).east(4);
            case CLOCKWISE_180:
                return pos.east(4).south(4);
            case COUNTERCLOCKWISE_90:
                return pos.south(4).west(4);
            default:
                return pos;
        }
    }
}