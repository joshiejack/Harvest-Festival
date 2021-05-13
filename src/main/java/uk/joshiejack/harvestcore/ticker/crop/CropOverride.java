package uk.joshiejack.harvestcore.ticker.crop;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface CropOverride {
    void tickCrop(World world, BlockPos pos, IBlockState state);
}
