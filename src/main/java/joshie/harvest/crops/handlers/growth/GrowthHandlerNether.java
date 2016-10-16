package joshie.harvest.crops.handlers.growth;

import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.crops.GrowthHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class GrowthHandlerNether extends GrowthHandler {
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(List<String> list, Crop crop, boolean debug) {
        list.add(TextFormatting.RED + "Nether");
    }

    @Override
    @Deprecated
    public boolean canSustainCrop(IBlockAccess world, BlockPos pos, IBlockState state, Crop crop) {
        return state.getBlock() == Blocks.SOUL_SAND && crop.getPlantType() == EnumPlantType.Nether;
    }

    @Override
    public boolean canGrow(World world, BlockPos pos, Crop crop) {
        return world.provider.getDimension() == -1;
    }
}