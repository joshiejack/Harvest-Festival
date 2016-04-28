package joshie.harvest.crops.handlers;

import joshie.harvest.api.crops.ISoilHandler;
import joshie.harvest.blocks.HFBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class SoilHandlerDefault implements ISoilHandler, IPlantable {
    private EnumPlantType plantable;
    private Block block;

    public SoilHandlerDefault(final EnumPlantType type, Block block) {
        this(type);
        this.block = block;
    }

    public SoilHandlerDefault(final EnumPlantType type) {
        this.plantable = type;
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, IPlantable plantable) {
        Block below = world.getBlockState(pos.down()).getBlock();
        if (block != null) {
            return below == block && below.canSustainPlant(state, world, pos.down(), EnumFacing.UP, this);
        } else return below.canSustainPlant(state, world, pos.down(), EnumFacing.UP, this);
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return plantable;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        return HFBlocks.CROPS.getDefaultState();
    }
}