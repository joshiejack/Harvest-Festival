package joshie.harvestmoon.api.registry;

import joshie.harvestmoon.api.Calendar.ISeason;
import joshie.harvestmoon.api.crops.ICrop;
import joshie.harvestmoon.api.crops.ICropData;
import net.minecraft.world.World;

public interface ICropRegistry {
    public ICrop registerCrop(String unlocalized, int cost, int sell, int stages, int regrow, int year, int color, ISeason... seasons);

    public ICrop registerCrop(ICrop crop);

    public ICrop getCrop(String unlocalized);
    
    public ICropData getCropAtLocation(World world, int x, int y, int z);
}
