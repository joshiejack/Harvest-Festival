package uk.joshiejack.harvestcore.world.mine.dimension.decorators;

import uk.joshiejack.harvestcore.world.mine.dimension.wrappers.AbstractDecoratorWrapper;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class DecoratorSnowLayer extends AbstractDecoratorModifyable {
    public DecoratorSnowLayer() {
        state = Blocks.SNOW_LAYER.getDefaultState();
        numberOfBlocks = 3;
    }

    @Override
    public void decorate(AbstractDecoratorWrapper world, BlockPos pos) {
        for (int i = -numberOfBlocks + world.rand.nextInt(numberOfBlocks); i <= numberOfBlocks - world.rand.nextInt(numberOfBlocks); i++) {
            for (int j = -numberOfBlocks + world.rand.nextInt(numberOfBlocks); j <= numberOfBlocks - world.rand.nextInt(numberOfBlocks); j++) {
                BlockPos target = pos.add(i, 0, j);
                if (world.isAirBlock(target)) {
                    world.setBlockState(target, state);
                }
            }
        }
    }
}
