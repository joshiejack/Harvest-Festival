package joshie.harvest.buildings.placeable;

import com.google.gson.annotations.Expose;
import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class Placeable {
    @Expose
    protected BlockPos pos;

    public Placeable init() {
        return this;
    }

    public BlockPos getOffsetPos() {
        return pos;
    }

    public int getX() {
        return pos.getX();
    }

    public int getY() {
        return pos.getY();
    }

    public int getZ() {
        return pos.getZ();
    }

    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.BUILD;
    }

    private void clearBushes(World world, BlockPos pos) {
        if (world.getBlockState(pos).getBlock() instanceof BlockBush) {
            world.setBlockToAir(pos);
            world.notifyNeighborsOfStateChange(pos, Blocks.AIR);
        }
    }

    public boolean place(World world, BlockPos pos, Rotation rotation, ConstructionStage stage, boolean playSound) {
        BlockPos transformed = getTransformedPosition(pos, rotation);
        if (world.getBlockState(transformed).getBlockHardness(world, transformed) == -1F) return true;
        if (canPlace(stage)) {
            if (stage == ConstructionStage.BUILD) clearBushes(world, transformed.up());
            return place(world, transformed, rotation, playSound);
        } else return false;
    }

    public BlockPos getTransformedPosition(BlockPos pos, Rotation rotation) {
        BlockPos adjusted = transformBlockPos(rotation);
        return new BlockPos(pos.getX() + adjusted.getX(), pos.getY() + adjusted.getY(), pos.getZ() + adjusted.getZ());
    }

    public BlockPos transformBlockPos(Rotation rotation) {
        int i = getX();
        int j = getY();
        int k = getZ();
        switch (rotation)  {
            case COUNTERCLOCKWISE_90:
                return new BlockPos(k, j, -i);
            case CLOCKWISE_90:
                return new BlockPos(-k, j, i);
            case CLOCKWISE_180:
                return new BlockPos(-i, j, -k);
            default:
                return getOffsetPos();
        }
    }

    public boolean place (World world, BlockPos pos, Rotation rotation, boolean playSound) {
        return false;
    }

    public void remove(World world, BlockPos pos, Rotation rotation, ConstructionStage stage, IBlockState replacement) {}

    public enum ConstructionStage {
        BUILD, PAINT, DECORATE, MOVEIN, FINISHED
    }
}