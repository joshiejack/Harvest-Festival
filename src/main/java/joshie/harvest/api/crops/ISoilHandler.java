package joshie.harvest.api.crops;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IPlantable;

public interface ISoilHandler {
    public boolean canSustainPlant(IBlockState state, IBlockAccess access, BlockPos pos, IPlantable plantable);
}