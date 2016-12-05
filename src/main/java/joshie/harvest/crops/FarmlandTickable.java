package joshie.harvest.crops;

import joshie.harvest.api.ticking.IDailyTickableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FarmlandTickable implements IDailyTickableBlock {
    private boolean hasNoCrops(World worldIn, BlockPos pos)  {
        Block block = worldIn.getBlockState(pos.up()).getBlock();
        return !(block instanceof net.minecraftforge.common.IPlantable);
    }

    @Override
    public boolean newDay(World world, BlockPos pos, IBlockState state) {
        if (CropHelper.isSoil(state)) {
            if (CropHelper.isRainingAt(world, pos.up(2))) {
                if (!CropHelper.isWetSoil(state)) world.setBlockState(pos, state.withProperty(BlockFarmland.MOISTURE, 7), 2);
            } else {
                int moisture = state.getValue(BlockFarmland.MOISTURE);
                if (moisture == 7) world.setBlockState(pos, state.withProperty(BlockFarmland.MOISTURE, 3), 2);
                else if (moisture == 3) world.setBlockState(pos, state.withProperty(BlockFarmland.MOISTURE, 0), 2);
                else if (moisture == 0 && hasNoCrops(world, pos)) world.setBlockState(pos, Blocks.DIRT.getDefaultState(), 2);
            }
        }

        return world.getBlockState(pos).getBlock() instanceof BlockFarmland;
    }
}
