package uk.joshiejack.harvestcore.world.mine.dimension.wrappers;

import uk.joshiejack.harvestcore.world.mine.tier.Tier;
import uk.joshiejack.penguinlib.util.BlockStates;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public class DecoratorWrapperChunkPrimer extends AbstractDecoratorWrapper {
    private final ChunkPrimer primer;
    public final Random rand;
    public final int floor;

    public DecoratorWrapperChunkPrimer(ChunkPrimer primer, Tier tier, Random rand, int floor) {
        super(tier, rand, floor);
        this.primer = primer;
        this.rand = rand;
        this.floor = floor;
    }

    @Override
    public boolean isAirBlock(BlockPos pos) {
        return primer.getBlockState(pos.getX(), pos.getY(), pos.getZ()) == BlockStates.AIR;
    }

    @Override
    public void setBlockState(BlockPos pos, IBlockState state) {
        primer.setBlockState(pos.getX(), pos.getY(), pos.getZ(), state);
    }

    @Override
    public IBlockState getBlockState(BlockPos pos) {
        return primer.getBlockState(pos.getX(), pos.getY(), pos.getZ());
    }
}
