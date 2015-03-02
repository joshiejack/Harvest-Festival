package joshie.harvestmoon.api.crops;

import joshie.harvestmoon.api.Calendar.ISeason;
import net.minecraft.world.World;

public interface ICropHandler {
    public ICrop registerCrop(String unlocalized, int cost, int sell, int stages, int regrow, int year, int color, ISeason... seasons);

    public ICrop registerCrop(ICrop crop);

    public ICrop getCrop(String unlocalized);
    
    /** Will NEVER return null, however it may have an instance of 'null_crop' **/
    public ICropData getCropAtLocation(World world, int x, int y, int z);
}
