package joshie.harvest.buildings.placeable;

import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public abstract class Placeable {
    private BlockPos offsetPos;

    public Placeable(BlockPos pos) {
        this.offsetPos = pos;
    }

    public BlockPos getOffsetPos() {
        return offsetPos;
    }

    public int getX() {
        return offsetPos.getX();
    }

    public int getY() {
        return offsetPos.getY();
    }

    public int getZ() {
        return offsetPos.getZ();
    }

    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.BUILD;
    }

    public boolean place(UUID owner, World world, BlockPos pos, Mirror mirror, Rotation rotation, ConstructionStage stage) {
        if (canPlace(stage)) {
            return place(owner, world, getTransformedPosition(pos, mirror, rotation) , mirror, rotation);
        } else return false;
    }

    public BlockPos getTransformedPosition(BlockPos pos, Mirror mirror, Rotation rotation) {
        BlockPos adjusted = transformBlockPos(mirror, rotation);
        return new BlockPos(pos.getX() + adjusted.getX(), pos.getY() + adjusted.getY(), pos.getZ() + adjusted.getZ());
    }

    private BlockPos transformBlockPos(Mirror mirrorIn, Rotation rotationIn) {
        int i = getX();
        int j = getY();
        int k = getZ();
        boolean flag = true;

        switch (mirrorIn) {
            case LEFT_RIGHT:
                k = -k;
                break;
            case FRONT_BACK:
                i = -i;
                break;
            default:
                flag = false;
        }

        switch (rotationIn)  {
            case COUNTERCLOCKWISE_90:
                return new BlockPos(k, j, -i);
            case CLOCKWISE_90:
                return new BlockPos(-k, j, i);
            case CLOCKWISE_180:
                return new BlockPos(-i, j, -k);
            default:
                return flag ? new BlockPos(i, j, k) : getOffsetPos();
        }
    }

    public boolean place (UUID owner, World world, BlockPos pos, Mirror mirror, Rotation rotation) {
        return false;
    }

    public static enum ConstructionStage {
        BUILD, PAINT, DECORATE, MOVEIN, FINISHED;
    }
}