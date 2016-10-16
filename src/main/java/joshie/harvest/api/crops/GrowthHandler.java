package joshie.harvest.api.crops;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public abstract class GrowthHandler {
    /** Add tooltips to the crops
     *  @param list         the tooltiplist
     *  @param crop         the crop
     *  @param debug        debug mode? **/
    @SideOnly(Side.CLIENT)
    public void addInformation(List<String> list, Crop crop, boolean debug) {
        for (Season season : crop.getSeasons()) {
            list.add(season.getDisplayName());
        }
    }

    @Deprecated //TODO: Remove in 0.7
    public boolean canSustainCrop(IBlockAccess world, BlockPos pos, IBlockState state, Crop crop) {
        return state.getBlock() == Blocks.FARMLAND && crop.getPlantType() == EnumPlantType.Crop;
    }

    /** Return true if this crop can't crop in this location
     *  @param world        the world
     *  @param pos          the position
     *  @param crop         the crop **/
    public boolean canGrow(World world, BlockPos pos, Crop crop) {
        Season toMatch = HFApi.calendar.getSeasonAtCoordinates(world, pos);
        if (crop.getSeasons() == null) return false;
        for (Season season : crop.getSeasons()) {
            if (toMatch == season) return true;
        }

        return false;
    }
}