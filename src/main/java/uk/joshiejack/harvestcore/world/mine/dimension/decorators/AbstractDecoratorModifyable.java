package uk.joshiejack.harvestcore.world.mine.dimension.decorators;

import net.minecraft.block.state.IBlockState;

public abstract class AbstractDecoratorModifyable extends AbstractDecorator {
    protected IBlockState state;
    protected int numberOfBlocks;

    protected AbstractDecoratorModifyable() {}

    public AbstractDecoratorModifyable setState(IBlockState state) {
        this.state = state;
        return this;
    }

    public AbstractDecoratorModifyable setNumber(int number) {
        this.numberOfBlocks = number;
        return this;
    }
}
