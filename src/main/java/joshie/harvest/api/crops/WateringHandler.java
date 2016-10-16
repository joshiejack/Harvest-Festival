package joshie.harvest.api.crops;

import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WateringHandler {
    private static final IBlockState WET_SOIL = Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, 7);

    public boolean isWet(IBlockState state) {
        return state == WET_SOIL;
    }

    public boolean canBeWatered(IBlockState state) {
        return state.getBlock() == Blocks.FARMLAND;
    }

    public void water(World world, BlockPos pos) {
        world.setBlockState(pos, WET_SOIL);
    }
}
