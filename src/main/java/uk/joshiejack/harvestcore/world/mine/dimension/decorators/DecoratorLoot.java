package uk.joshiejack.harvestcore.world.mine.dimension.decorators;

import uk.joshiejack.harvestcore.block.HCBlocks;
import uk.joshiejack.harvestcore.world.mine.dimension.wrappers.AbstractDecoratorWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class DecoratorLoot extends AbstractDecorator {
    private final IBlockState state;
    private final int minimumFloor;
    private final int maximumFloor;
    private int clusterDistance;
    private int clusterSize;

    public DecoratorLoot(IBlockState state, int min) {
        this(state, min, Integer.MAX_VALUE);
    }
    public DecoratorLoot(IBlockState state, int min, int max) {
        this.state = state;
        this.minimumFloor = min;
        this.maximumFloor = max;
    }

    public IBlockState getState() {
        return state;
    }

    public void setClustered(int distance, int amount) {
        this.clusterDistance = distance;
        this.clusterSize = amount;
    }

    public boolean isValid(AbstractDecoratorWrapper world, BlockPos pos) {
        for (EnumFacing facing: EnumFacing.HORIZONTALS) {
            for (int i = 1; i <= 2; i++) {
                BlockPos target = pos.offset(facing, i);
                if (world.hasBuffer(target, 0) && (world.getBlockState(target).getBlock() ==  world.tier.getPortal(world.floor) || world.getBlockState(target).getBlock() == HCBlocks.ELEVATOR)) {
                    return false;
                }
            }
        }

        return world.floor >= minimumFloor && world.floor <= maximumFloor;
    }

    @Override
    public void decorate(AbstractDecoratorWrapper world, BlockPos pos) {
        if (clusterDistance > 0) {
            for (int i = 0; i < clusterSize; ++i) {
                BlockPos blockpos = pos.add(world.rand.nextInt(clusterDistance) - world.rand.nextInt(clusterDistance), 0, world.rand.nextInt(clusterDistance) - world.rand.nextInt(clusterDistance));
                if (world.hasBuffer(blockpos, 1) && world.isAirBlock(blockpos) && world.getBlockState(blockpos.down()).isFullCube()) {
                    world.setBlockState(blockpos, state);
                }
            }
        }

        world.setBlockState(pos, state);
    }

    public static class Special {
        public DecoratorLoot loot;
        public int negative;
        public double divider;

        public Special(IBlockState state, int negative, double divider) {
            this.loot = new DecoratorLoot(state, 1);
            this.negative = negative;
            this.divider = divider;
        }
    }
}
