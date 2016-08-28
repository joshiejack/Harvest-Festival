package joshie.harvest.crops.handlers.growth;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.crops.IGrowthHandler;
import joshie.harvest.calendar.CalendarAPI;
import joshie.harvest.calendar.SeasonData;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class GrowthHandlerSeasonal implements IGrowthHandler {
    private EnumPlantType plantType;
    private Block block;

    public GrowthHandlerSeasonal(EnumPlantType type, Block block) {
        this.plantType = type;
        this.block = block;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(List<String> list, ICrop crop, boolean debug) {
        for (Season season : crop.getSeasons()) {
            SeasonData data = CalendarAPI.INSTANCE.getDataForSeason(season);
            list.add(data.getTextColor() + data.getLocalized());
        }
    }

    @Override
    public boolean canSustainCrop(IBlockAccess world, BlockPos pos, IBlockState state, ICrop crop) {
        return state.getBlock() == block && crop.getPlantType() == plantType;
    }

    @Override
    public boolean canGrow(World world, BlockPos pos, ICrop crop) {
        Season toMatch = HFApi.calendar.getSeasonAtCoordinates(world, pos);
        if (crop.getSeasons() == null) return false;
        for (Season season : crop.getSeasons()) {
            if (toMatch == season) return true;
        }

        return false;
    }
}