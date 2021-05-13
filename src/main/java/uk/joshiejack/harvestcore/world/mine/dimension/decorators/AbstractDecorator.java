package uk.joshiejack.harvestcore.world.mine.dimension.decorators;

import uk.joshiejack.harvestcore.world.mine.dimension.wrappers.AbstractDecoratorWrapper;
import net.minecraft.util.math.BlockPos;

public abstract class AbstractDecorator {
    public abstract void decorate(AbstractDecoratorWrapper world, BlockPos pos);
}
