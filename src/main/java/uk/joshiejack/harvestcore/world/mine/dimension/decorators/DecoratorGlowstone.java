package uk.joshiejack.harvestcore.world.mine.dimension.decorators;

import uk.joshiejack.harvestcore.world.mine.dimension.wrappers.AbstractDecoratorWrapper;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class DecoratorGlowstone extends AbstractDecoratorModifyable {
    public DecoratorGlowstone() {
        state = Blocks.GLOWSTONE.getDefaultState();
        numberOfBlocks = 1500;
    }

    @Override
    public void decorate(AbstractDecoratorWrapper worldIn, BlockPos position) {
        position = position.up(3);
        worldIn.setBlockState(position, state);

        for (int i = 0; i < numberOfBlocks; ++i) {
            BlockPos blockpos = position.add(worldIn.rand.nextInt(8) - worldIn.rand.nextInt(8), -worldIn.rand.nextInt(12), worldIn.rand.nextInt(8) - worldIn.rand.nextInt(8));

            if (worldIn.isAirBlock(blockpos)) {
                int j = 0;

                for (EnumFacing enumfacing : EnumFacing.values()) {
                    if (worldIn.getBlockState(blockpos.offset(enumfacing)).getBlock() == state.getBlock()) {
                        ++j;
                    }

                    if (j > 1) {
                        break;
                    }
                }

                if (j == 1 && blockpos.getY() > 2) {
                    worldIn.setBlockState(blockpos, state);
                }
            }
        }
    }
}
