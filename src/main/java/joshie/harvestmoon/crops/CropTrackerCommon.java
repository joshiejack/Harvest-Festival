package joshie.harvestmoon.crops;

import java.util.HashMap;

import joshie.harvestmoon.api.WorldLocation;
import joshie.harvestmoon.api.crops.ICrop;
import joshie.harvestmoon.api.crops.ICropData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CropTrackerCommon {
    protected HashMap<WorldLocation, ICropData> crops = new HashMap();

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
        ICropData data = crops.get(location);
        return data != null ? data : new CropData(location);
    }

    public boolean canBonemeal(World world, int x, int y, int z) {
        ICropData data = getCropDataForLocation(world, x, y, z);
        return data.getStage() < data.getCrop().getStages();
    }

    /* Plants a crop at this location */
    public boolean plantCrop(EntityPlayer player, World world, int x, int y, int z, ICrop crop, int quality, int stage) {
        return true;
    }

    /* Returns the product you get from this plants */
    public ItemStack getHarvest(EntityPlayer player, World world, int x, int y, int z) {
        ICropData data = getCropDataForLocation(world, x, y, z);
        return data.harvest(player, false);
    }

    /* Harvests the plant */
    public ItemStack harvest(EntityPlayer player, World world, int x, int y, int z) {
        ICropData data = getCropDataForLocation(world, x, y, z);
        ItemStack harvest = data.harvest(player, true);
        if (harvest != null) {
            if (data.getCrop().getRegrowStage() < 0) {
                removeCrop(world, x, y, z);
            }

            return harvest;
        } else return null;
    }

    /* Hydrates this plant for the day */
    public void hydrate(World world, int x, int y, int z) {}
    
    public void setWithered(ICropData data) {}
    
    /* Clear Crop */
    public void removeCrop(World world, int x, int y, int z) {
        ICropData data = getCropDataForLocation(world, x, y, z);
        crops.remove(data.getLocation());
    }
}
