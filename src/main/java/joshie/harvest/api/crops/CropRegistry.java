package joshie.harvest.api.crops;

import java.util.Collection;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.crops.ICropRenderHandler.PlantSection;
import joshie.harvest.blocks.BlockCrop;
import joshie.harvest.core.helpers.CropHelper;
import joshie.harvest.crops.Crop;
import joshie.harvest.init.HFConfig;
import net.minecraft.world.World;

public class CropRegistry implements ICropHandler {
    @Override
    public ICrop getCrop(String unlocalized) {
        return HFConfig.mappings.getCrop(unlocalized);
    }

    @Override
    public ICropData getCropAtLocation(World world, int x, int y, int z) {
        PlantSection section = BlockCrop.getSection(world, x, y, z);
        return section == PlantSection.BOTTOM ? CropHelper.getCropAtLocation(world, x, y, z) : CropHelper.getCropAtLocation(world, x, y - 1, z);
    }

    @Override
    public ICrop registerCrop(String unlocalized, int cost, int sell, int stages, int regrow, int year, int color, Season... seasons) {
        return new Crop(unlocalized, seasons, cost, sell, stages, regrow, year, color);
    }

    @Override
    public ICrop registerCrop(ICrop crop) {
        return crop;
    }

    @Override
    public Collection<ICrop> getCrops() {
        return Crop.crops;
    }
}
