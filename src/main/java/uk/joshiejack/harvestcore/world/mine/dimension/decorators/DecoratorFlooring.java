package uk.joshiejack.harvestcore.world.mine.dimension.decorators;

import uk.joshiejack.harvestcore.world.mine.dimension.wrappers.AbstractDecoratorWrapper;
import net.minecraft.util.math.BlockPos;

public class DecoratorFlooring extends AbstractDecoratorModifyable {
    @Override
    public void decorate(AbstractDecoratorWrapper world, BlockPos pos) {
        for (int i = -3 + world.rand.nextInt(3); i <= 3 - world.rand.nextInt(3); i++) {
            for (int j = -3 + world.rand.nextInt(3); j <= 3 - world.rand.nextInt(3); j++) {
                BlockPos target = pos.add(i, 0, j);
                if (world.isAirBlock(target) && !world.getBlockState(target.down()).getMaterial().isLiquid()) {
                    world.setBlockState(target, state);
                }
            }
        }
    }
}
