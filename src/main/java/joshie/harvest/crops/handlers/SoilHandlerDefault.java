package joshie.harvest.crops.handlers;

import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.crops.ISoilHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;

public class SoilHandlerDefault implements ISoilHandler {
    private EnumPlantType plantType;
    private Block block;

    public SoilHandlerDefault(EnumPlantType type, Block block) {
        this.plantType = type;
        this.block = block;
    }

    @Override
    public boolean canSustainCrop(IBlockAccess world, BlockPos pos, IBlockState state, ICrop crop) {
        return state.getBlock() == block && crop.getPlantType() == plantType;
    }
}