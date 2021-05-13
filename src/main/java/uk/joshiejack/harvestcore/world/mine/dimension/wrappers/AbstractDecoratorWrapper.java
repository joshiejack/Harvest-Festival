package uk.joshiejack.harvestcore.world.mine.dimension.wrappers;

import uk.joshiejack.harvestcore.world.mine.tier.Tier;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public abstract class AbstractDecoratorWrapper {
    public final Tier tier;
    public final Random rand;
    public final int floor;

    public AbstractDecoratorWrapper(Tier tier, Random rand, int floor) {
        this.tier = tier;
        this.rand = rand;
        this.floor = Math.max(1, floor);
    }

    public boolean isWallBlock(BlockPos pos) {
        return getBlockState(pos) == tier.getWall(floor);
    }

    public abstract boolean isAirBlock(BlockPos pos);

    public abstract void setBlockState(BlockPos pos, IBlockState state);

    public boolean hasBuffer(BlockPos pos, int buffer) {
        return pos.getX() - buffer >= 0 && pos.getX() + buffer <= 15 && pos.getZ() - buffer >= 0 && pos.getZ() + buffer <= 15;
    }

    public abstract IBlockState getBlockState(BlockPos pos);

    public boolean isNextTo(BlockPos target, IBlockState state) {
        return getBlockState(target.east()) == state || getBlockState(target.west()) == state || getBlockState(target.north()) == state || getBlockState(target.south()) == state;
    }
}
