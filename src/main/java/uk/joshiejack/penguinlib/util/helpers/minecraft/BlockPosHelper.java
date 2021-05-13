package uk.joshiejack.penguinlib.util.helpers.minecraft;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

public class BlockPosHelper {
    public static BlockPos getTransformedPosition(BlockPos target, BlockPos original, Rotation rotation) {
        BlockPos adjusted = transformBlockPos(target, rotation);
        return new BlockPos(original.getX() + adjusted.getX(), original.getY() + adjusted.getY(), original.getZ() + adjusted.getZ());
    }

    public static BlockPos transformBlockPos(BlockPos target, Rotation rotation) {
        int i = target.getX();
        int j = target.getY();
        int k = target.getZ();
        switch (rotation)  {
            case COUNTERCLOCKWISE_90:
                return new BlockPos(k, j, -i);
            case CLOCKWISE_90:
                return new BlockPos(-k, j, i);
            case CLOCKWISE_180:
                return new BlockPos(-i, j, -k);
            default:
                return target;
        }
    }
}
