package uk.joshiejack.penguinlib.template;

import com.google.common.collect.Sets;
import uk.joshiejack.penguinlib.util.helpers.minecraft.BlockPosHelper;
import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Set;

public abstract class Placeable {
    protected BlockPos pos;

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

    public Set<BlockPos> getPositions(BlockPos transformed) {
        return Sets.newHashSet(transformed);
    }

    private void clearBushes(World world, BlockPos pos) {
        if (world.getBlockState(pos).getBlock() instanceof BlockBush) {
            world.setBlockToAir(pos);
            world.notifyNeighborsOfStateChange(pos, Blocks.AIR, false);
        }
    }

    public boolean place(World world, BlockPos pos, Rotation rotation, ConstructionStage stage, boolean playSound) {
        return place(world, pos, rotation, stage, playSound, PlaceableHelper.DEFAULT);
    }

    public boolean place(World world, BlockPos pos, Rotation rotation, ConstructionStage stage, boolean playSound, Template.Replaceable replaceable) {
        BlockPos transformed = BlockPosHelper.getTransformedPosition(this.pos, pos, rotation);
        if (!replaceable.canReplace(world, transformed)) return true;
        if (canPlace(stage)) {
            if (stage == ConstructionStage.BUILD) clearBushes(world, transformed.up());
            return place(world, transformed, rotation, playSound);
        } else return false;
    }

    protected boolean place (World world, BlockPos pos, Rotation rotation, boolean playSound) {
        return false;
    }

    public void remove(World world, BlockPos pos, Rotation rotation, ConstructionStage stage, IBlockState replacement) {}

    public enum ConstructionStage {
        BUILD, PAINT, DECORATE, MOVEIN, FINISHED;

        @Nullable
        public ConstructionStage next() {
            return this == FINISHED ? FINISHED : ConstructionStage.values()[this.ordinal() + 1];
        }

        public ConstructionStage previous() {
            return this == BUILD ? FINISHED : ConstructionStage.values()[this.ordinal() - 1];
        }
    }
}