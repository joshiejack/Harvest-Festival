package uk.joshiejack.harvestcore.world.mine.dimension.wrappers;

import uk.joshiejack.harvestcore.world.mine.tier.Tier;
import uk.joshiejack.penguinlib.util.BlockStates;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

import static uk.joshiejack.harvestcore.world.mine.dimension.MineChunkGenerator.MAX_XZ_PER_SECTION;

public class DecorateWrapperBlockStateMap extends AbstractDecoratorWrapper {
    private final IBlockState[][][] blockStateMap;
    public DecorateWrapperBlockStateMap(IBlockState[][][] blockStateMap, Tier tier,  Random rand, int floor) {
        super(tier, rand, floor);
        this.blockStateMap = blockStateMap;
    }

    private boolean isValid(BlockPos pos) {
        return pos.getX() > 0 && pos.getZ() > 0 && pos.getY() >= 0 &&
                pos.getX() < MAX_XZ_PER_SECTION - 1 && pos.getZ() < MAX_XZ_PER_SECTION - 1 && pos.getY() < 6;
    }

    @Override
    public boolean isWallBlock(BlockPos pos) {
        return getBlockState(pos) == tier.getWall(floor);
    }

    @Override
    public boolean isAirBlock(BlockPos pos) {
        return isValid(pos) && blockStateMap[pos.getX()][pos.getZ()][pos.getY()] == BlockStates.AIR;
    }

    @Override
    public void setBlockState(BlockPos pos, IBlockState state) {
        if (isValid(pos)) {
            blockStateMap[pos.getX()][pos.getZ()][pos.getY()] = state;
        }
    }

    @Override
    public IBlockState getBlockState(BlockPos pos) {
        if (!isValid(pos)) return BlockStates.BEDROCK;
        else {
            return blockStateMap[pos.getX()][pos.getZ()][pos.getY()];
        }
    }

    @Override
    public boolean hasBuffer(BlockPos pos, int buffer) {
        return pos.getX() - buffer > 0 && pos.getX() + buffer < MAX_XZ_PER_SECTION - 1 && pos.getZ() - buffer > 0 && pos.getZ() + buffer < MAX_XZ_PER_SECTION - 1;
    }
}
