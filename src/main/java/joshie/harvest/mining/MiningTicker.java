package joshie.harvest.mining;

import joshie.harvest.api.core.IDailyTickableBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MiningTicker implements IDailyTickableBlock {
    @Override
    public boolean newDay(World world, BlockPos pos, IBlockState state) {
        BlockPos up = pos.up();
        IBlockState above = world.getBlockState(up);
        if (above.getBlock() == Blocks.AIR) {
            world.setBlockState(up, Blocks.DIAMOND_BLOCK.getDefaultState());
        }

        return true;
    }
}
