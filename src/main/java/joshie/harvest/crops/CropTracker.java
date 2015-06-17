package joshie.harvest.crops;

import java.util.HashMap;

import joshie.harvest.api.WorldLocation;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.crops.ICropData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CropTracker {
    protected HashMap<WorldLocation, ICropData> crops = new HashMap();

    protected WorldLocation getCropKey(World world, int x, int y, int z) {
        return new WorldLocation(world.provider.dimensionId, x, y, z);
    }

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

    public boolean plantCrop(EntityPlayer player, World world, int x, int y, int z, ICrop crop, int stage) {
        return true;
    }

    public ItemStack getHarvest(EntityPlayer player, World world, int x, int y, int z) {
        ICropData data = getCropDataForLocation(world, x, y, z);
        return data.harvest(player, false);
    }

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

    public void removeCrop(World world, int x, int y, int z) {
        ICropData data = getCropDataForLocation(world, x, y, z);
        crops.remove(data.getLocation());
    }

    public void hydrate(World world, int x, int y, int z) {}

    public void setWithered(ICropData data) {}

    public void grow(World world, int x, int y, int z) {}

    public void newDay() {}

    public void sendUpdateToClient(EntityPlayerMP player, World world, int x, int y, int z) {}

    public void updateClient(boolean isRemoval, WorldLocation location, ICropData data) {}

    public void doRain() {}
}
