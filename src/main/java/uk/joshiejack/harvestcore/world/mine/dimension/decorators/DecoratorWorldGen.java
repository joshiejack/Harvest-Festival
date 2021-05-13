package uk.joshiejack.harvestcore.world.mine.dimension.decorators;

import uk.joshiejack.harvestcore.world.mine.dimension.wrappers.AbstractDecoratorWrapper;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public abstract class DecoratorWorldGen extends AbstractDecorator {
    @Override
    public void decorate(AbstractDecoratorWrapper world, BlockPos pos) {
        decorate(world, world.rand, pos);
    }

    protected abstract void decorate(AbstractDecoratorWrapper worldIn, Random rand, BlockPos position);
}
