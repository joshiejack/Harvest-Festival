package joshie.harvestmoon.crops;

import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CropTrackerClient {
    private HashMap<CropLocation, CropData> crops = new HashMap();

    private CropLocation getKey(World world, int x, int y, int z) {
        return new CropLocation(world.provider.dimensionId, x, y, z);
    }

    public boolean isCropGiant(World world, int x, int y, int z) {
        return crops.get(getKey(world, x, y, z)).isGiant();
    }

    public int getCropStage(World world, int x, int y, int z) {
        return crops.get(getKey(world, x, y, z)).getStage();
    }

    public String getCropName(World world, int x, int y, int z) {
        CropData data = crops.get(getKey(world, x, y, z));
        return data == null ? null : data.getName();
    }

    public ItemStack harvest(World world, int x, int y, int z) {
        CropLocation key = new CropLocation(world.provider.dimensionId, x, y, z);
        CropData data = crops.get(key);
        if (data == null) return null;
        else {
            ItemStack ret = data.harvest();
            if (ret != null) {
                if (!data.doesRegrow()) {
                    crops.remove(key);
                }

                return ret;
            } else return null;
        }
    }

    public void sync(boolean isRemoval, CropLocation location, CropData data) {
        if (isRemoval) {
            crops.remove(location);
        } else crops.put(location, data);
    }
}
