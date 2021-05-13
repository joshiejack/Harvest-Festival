package uk.joshiejack.harvestcore.world.mine.dimension.decorators;

import uk.joshiejack.harvestcore.world.mine.dimension.wrappers.AbstractDecoratorWrapper;
import net.minecraft.util.math.BlockPos;

public class DecoratorPillar extends AbstractDecoratorModifyable {
    @Override
    public void decorate(AbstractDecoratorWrapper world, BlockPos pos) {
        for (int y = 0; y <= 1 + world.rand.nextInt(3); y++) {
            BlockPos target = pos.up(y);
            if (world.isAirBlock(target)) {
                world.setBlockState(target, state);
            } else break;
        }
    }
}
