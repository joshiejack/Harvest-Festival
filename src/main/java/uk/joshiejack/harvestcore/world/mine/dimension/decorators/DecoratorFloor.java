package uk.joshiejack.harvestcore.world.mine.dimension.decorators;

import uk.joshiejack.harvestcore.world.mine.dimension.wrappers.AbstractDecoratorWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class DecoratorFloor extends AbstractDecoratorModifyable {
    public DecoratorFloor() {}
    public DecoratorFloor(IBlockState state, int numberOfBlocks) {
        this.state = state;
        this.numberOfBlocks = numberOfBlocks;
    }

    @Override
    public void decorate(AbstractDecoratorWrapper world, BlockPos position) {
        Random rand = world.rand;
        float f = rand.nextFloat() * (float) Math.PI;
        double d0 = (double) ((float) (position.getX() + 8) + MathHelper.sin(f) * (float) numberOfBlocks / 8.0F);
        double d1 = (double) ((float) (position.getX() + 8) - MathHelper.sin(f) * (float) numberOfBlocks / 8.0F);
        double d2 = (double) ((float) (position.getZ() + 8) + MathHelper.cos(f) * (float) numberOfBlocks / 8.0F);
        double d3 = (double) ((float) (position.getZ() + 8) - MathHelper.cos(f) * (float) numberOfBlocks / 8.0F);
        double d4 = (double) (position.getY() + rand.nextInt(3) - 2);
        double d5 = (double) (position.getY() + rand.nextInt(3) - 2);

        for (int i = 0; i < numberOfBlocks; ++i) {
            float f1 = (float) i / (float) numberOfBlocks;
            double d6 = d0 + (d1 - d0) * (double) f1;
            double d7 = d4 + (d5 - d4) * (double) f1;
            double d8 = d2 + (d3 - d2) * (double) f1;
            double d9 = rand.nextDouble() * (double) numberOfBlocks / 16.0D;
            double d10 = (double) (MathHelper.sin((float) Math.PI * f1) + 1.0F) * d9 + 1.0D;
            double d11 = (double) (MathHelper.sin((float) Math.PI * f1) + 1.0F) * d9 + 1.0D;
            int j = MathHelper.floor(d6 - d10 / 2.0D);
            int l = MathHelper.floor(d8 - d10 / 2.0D);
            int i1 = MathHelper.floor(d6 + d10 / 2.0D);
            int k1 = MathHelper.floor(d8 + d10 / 2.0D);

            for (int l1 = j; l1 <= i1; ++l1) {
                double d12 = ((double) l1 + 0.5D - d6) / (d10 / 2.0D);
                if (d12 * d12 < 1.0D) {
                    double d13 = ((double) 0 + 0.5D - d7) / (d11 / 2.0D);
                    if (d12 * d12 + d13 * d13 < 1.0D) {
                        for (int j2 = l; j2 <= k1; ++j2) {
                            double d14 = ((double) j2 + 0.5D - d8) / (d10 / 2.0D);

                            if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D) {
                                BlockPos blockpos = new BlockPos(l1, 0, j2);
                                if (world.getBlockState(blockpos) == world.tier.getFloor(world.floor) &&
                                        world.getBlockState(blockpos.up()) != world.tier.getFloor(world.floor)) {
                                    world.setBlockState(blockpos, this.state);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


}
