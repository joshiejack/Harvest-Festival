package uk.joshiejack.harvestcore.world.mine.dimension.decorators;

import uk.joshiejack.harvestcore.world.mine.dimension.wrappers.AbstractDecoratorWrapper;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class DecoratorTallGrass extends AbstractDecoratorModifyable {
    @Override
    public void decorate(AbstractDecoratorWrapper worldIn, BlockPos position) {
        for (int i = 0; i < 128; ++i) {
            BlockPos blockpos = position.add(worldIn.rand.nextInt(8) - worldIn.rand.nextInt(8), 0, worldIn.rand.nextInt(8) - worldIn.rand.nextInt(8));
            if (worldIn.isAirBlock(blockpos) && worldIn.getBlockState(blockpos.down()).getBlock() == Blocks.GRASS) {
                worldIn.setBlockState(blockpos, state);
            }
        }
    }
}
