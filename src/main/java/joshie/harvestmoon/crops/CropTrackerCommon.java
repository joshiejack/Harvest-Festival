package joshie.harvestmoon.crops;

import java.util.HashMap;

import joshie.harvestmoon.api.crops.ICropData;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CropTrackerCommon {
    protected HashMap<WorldLocation, CropData> crops = new HashMap();
    
    //Returns the location for crops
    protected WorldLocation getCropKey(World world, int x, int y, int z) {
        return new WorldLocation(world.provider.dimensionId, x, y, z);
    }
    
    //Returns the location for farmland
    protected WorldLocation getFarmlandKey(World world, int x, int y, int z) {
        return new WorldLocation(world.provider.dimensionId, x, y + 1, z);
    }
    
    public ICropData getCropDataForLocation(World world, int x, int y, int z) {
        WorldLocation location = getCropKey(world, x, y, z);
        CropData data = crops.get(location);
        return data != null? data : new CropData(location);
    }
}
