package uk.joshiejack.harvestcore.world.mine.dimension.wrappers;

import uk.joshiejack.harvestcore.world.mine.tier.Tier;
import uk.joshiejack.penguinlib.util.BlockStates;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import java.util.Random;

public class DecoratorWrapperExtendedBlockStorage extends AbstractDecoratorWrapper {
    private ExtendedBlockStorage storage;

    public DecoratorWrapperExtendedBlockStorage(ExtendedBlockStorage storage, Tier tier, Random rand, int floor) {
        super(tier, rand, floor);
        this.storage = storage;
    }

    @Override
    public boolean isAirBlock(BlockPos pos) {
        return storage.get(pos.getX(), pos.getY() & 15, pos.getZ()) == BlockStates.AIR;
    }

    @Override
    public void setBlockState(BlockPos pos, IBlockState state) {
        storage.set(pos.getX(), pos.getY() & 15, pos.getZ(), state);
    }

    @Override
    public IBlockState getBlockState(BlockPos pos) {
        return storage.get(pos.getX(), pos.getY() & 15, pos.getZ());
    }
}
