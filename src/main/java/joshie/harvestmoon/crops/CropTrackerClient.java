package joshie.harvestmoon.crops;

import joshie.harvestmoon.api.WorldLocation;
import joshie.harvestmoon.api.crops.ICropData;
import joshie.harvestmoon.core.network.PacketCropRequest;
import joshie.harvestmoon.core.network.PacketHandler;
import joshie.harvestmoon.init.HMItems;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CropTrackerClient extends CropTrackerCommon {
    @Override
    public ICropData getCropDataForLocation(World world, int x, int y, int z) {
        WorldLocation location = getCropKey(world, x, y, z);
        ICropData data = crops.get(location);
        if (data == null) {
            PacketHandler.sendToServer(new PacketCropRequest(world, x, y, z));
        }
        
        return data != null? data: new CropData(location);
    }

    public ItemStack getStackForCrop(World world, int x, int y, int z) {
        WorldLocation key = getCropKey(world, x, y, z);
        ICropData data = crops.get(key);
        if (data == null) return new ItemStack(Blocks.cactus); //Because why not?
        ItemStack seeds = new ItemStack(HMItems.seeds);
        seeds.setItemDamage(data.getCrop().getCropMeta() + ((data.getQuality() - 1) * 100));
        return seeds;
    }

    public void sync(boolean isRemoval, WorldLocation location, ICropData data) {
        if (isRemoval) {
            crops.remove(location);
        } else crops.put(location, data);
    }
}
