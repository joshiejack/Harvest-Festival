package joshie.harvest.core.helpers.generic;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class MCServerHelper {
    public static World getWorld(int dimension) {
        return DimensionManager.getWorld(dimension);
    }

    public static void markForUpdate(World world, BlockPos pos) {
        markForUpdate(world, pos, world.getBlockState(pos));
    }

    public static void markForUpdate(World world, BlockPos pos, IBlockState state) {
        markForUpdate(world, pos, state, 2);
    }

    public static void markForUpdate(World world, BlockPos pos, IBlockState state, int value) {
        world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), state, state, value);
    }

    public static void markForUpdate(World world, BlockPos pos, int value) {
        markForUpdate(world, pos, world.getBlockState(pos), value);
    }
}
