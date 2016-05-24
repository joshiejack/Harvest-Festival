package joshie.harvest.api.crops;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface ISoilHandler {
    boolean canSustainCrop(IBlockAccess world, BlockPos pos, IBlockState state, ICrop crop);
}