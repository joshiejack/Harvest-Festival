package joshie.harvest.crops.handlers.growth;

import joshie.harvest.crops.HFCrops;
import joshie.harvest.crops.block.BlockFruit.Fruit;
import joshie.harvest.crops.block.BlockLeavesFruit.LeavesFruit;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("unused")
public class GrowthHandlerApple extends GrowthHandlerHFTree {
    public GrowthHandlerApple() {
        super(Fruit.APPLE);
    }

    @Override
    protected boolean isLeaves(IBlockState state) {
        return state.getBlock() == HFCrops.LEAVES_FRUIT && HFCrops.LEAVES_FRUIT.getEnumFromState(state) == LeavesFruit.APPLE;
    }

    @Override
    protected BlockPos getAdjustedPositionBasedOnRotation(BlockPos pos, Rotation rotation) {
        switch (rotation) {
            case NONE:
                return pos.west(34).north(4);
            case CLOCKWISE_90:
                return pos.north(3).east(4);
            case CLOCKWISE_180:
                return pos.east(3).south(4);
            case COUNTERCLOCKWISE_90:
                return pos.south(3).west(4);
            default:
                return pos;
        }
    }
}