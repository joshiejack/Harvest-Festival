package joshie.harvest.plugins.immersiveengineering;

import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.crops.GrowthHandler;
import joshie.harvest.core.helpers.TextHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class HempGrowthHandler extends GrowthHandler<Crop> {
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(List<String> list, Crop crop, boolean debug) {
        list.add(TextFormatting.LIGHT_PURPLE  + "" + TextFormatting.ITALIC + TextHelper.translate("crop.hemp.tooltip"));
        super.addInformation(list, crop, debug);
    }

    @Override
    public boolean canGrow(World world, BlockPos pos, Crop crop) {
        return super.canGrow(world, pos, crop) && world.getLight(pos) >= 12;
    }
}
