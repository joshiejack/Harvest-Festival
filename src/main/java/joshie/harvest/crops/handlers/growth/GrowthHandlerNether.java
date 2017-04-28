package joshie.harvest.crops.handlers.growth;

import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.crops.GrowthHandler;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class GrowthHandlerNether extends GrowthHandler<Crop> {
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(List<String> list, Crop crop, boolean debug) {
        list.add(TextFormatting.RED + "Nether");
    }

    @Override
    @SuppressWarnings("deprecation")
    public int grow(World world, BlockPos pos, Crop crop, int stage) {
        int newStage = super.grow(world, pos, crop, stage);
        world.setBlockState(pos, Blocks.NETHER_WART.getStateFromMeta(newStage), 2);
        return newStage; //Remain the same
    }

    @Override
    public boolean canGrow(World world, BlockPos pos, Crop crop) {
        return true;
        //TODO: Readd return world.provider.getDimension() == -1;
    }
}