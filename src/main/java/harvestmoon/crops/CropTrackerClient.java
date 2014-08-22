package harvestmoon.crops;

import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CropTrackerClient {
    private static HashMap<CropLocation, CropData> crops = new HashMap();
    
    @SideOnly(Side.CLIENT)
    private CropLocation getKey(World world, int x, int y, int z) {
        return new CropLocation(world.provider.dimensionId, x, y, z);
    }

    @SideOnly(Side.CLIENT)
    public boolean isCropGiant(World world, int x, int y, int z) {
        return crops.get(getKey(world, x, y, z)).isGiant();
    }
    
    @SideOnly(Side.CLIENT)
    public int getCropStage(World world, int x, int y, int z) {
        return crops.get(getKey(world, x, y, z)).getStage();
    }
    
    @SideOnly(Side.CLIENT)
    public String getCropName(World world, int x, int y, int z) {
        CropData data = crops.get(getKey(world, x, y, z));
        return data == null? null: data.getName();
    }

    @SideOnly(Side.CLIENT)
    public ItemStack harvest(World world, int x, int y, int z) {
        CropLocation key = new CropLocation(world.provider.dimensionId, x, y, z);
        CropData data = crops.get(key);
        if(data == null) return null;
        else {
            ItemStack ret = data.harvest();
            if(ret != null) {
                if(!data.doesRegrow()) {
                    crops.remove(key);
                }
                
                return ret;
            } else return null;
        }
    }

    @SideOnly(Side.CLIENT)
    public void sync(boolean isRemoval, CropLocation location, CropData data) {
        if(isRemoval) {
            crops.remove(location);
        } else crops.put(location, data);
    }
}
