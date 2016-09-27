package joshie.harvest.buildings.placeable;

import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.util.Direction;
import net.minecraft.block.BlockBush;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class Placeable {
    protected int x, y, z;

    public Placeable init() {
        return this;
    }

    public BlockPos getOffsetPos() {
        return new BlockPos(x, y, z);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.BUILD;
    }

    public boolean isBlocked(World world, BlockPos pos) {
        return EntityHelper.getEntities(EntityLivingBase.class, world, pos, 0D, 0D).size() != 0;
    }

    private void clearBushes(World world, BlockPos pos) {
        if (world.getBlockState(pos).getBlock() instanceof BlockBush) {
            world.setBlockToAir(pos);
            world.notifyNeighborsOfStateChange(pos, Blocks.AIR);
        }
    }

    public boolean place(World world, BlockPos pos, Direction direction, ConstructionStage stage) {
        if (world.getBlockState(pos).getBlockHardness(world, pos) == -1F) return true;
        if (canPlace(stage)) {
            BlockPos transformed = getTransformedPosition(pos, direction);
            clearBushes(world, transformed.up());
            return place(world, transformed, direction);
        } else return false;
    }

    public BlockPos getTransformedPosition(BlockPos pos, Direction direction) {
        BlockPos adjusted = transformBlockPos(direction);
        return new BlockPos(pos.getX() + adjusted.getX(), pos.getY() + adjusted.getY(), pos.getZ() + adjusted.getZ());
    }

    public BlockPos transformBlockPos(Direction direction) {
        int i = getX();
        int j = getY();
        int k = getZ();
        boolean flag = true;

        switch (direction.getMirror()) {
            case LEFT_RIGHT:
                k = -k;
                break;
            case FRONT_BACK:
                i = -i;
                break;
            default:
                flag = false;
        }

        switch (direction.getRotation())  {
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

    public boolean place (World world, BlockPos pos, Direction direction) {
        return false;
    }

    public enum ConstructionStage {
        BUILD, PAINT, DECORATE, MOVEIN, FINISHED
    }
}