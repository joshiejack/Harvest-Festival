package joshie.harvestmoon.crops;

import joshie.harvestmoon.api.WorldLocation;
import joshie.harvestmoon.api.crops.ICropData;
import joshie.harvestmoon.core.network.PacketCropRequest;
import joshie.harvestmoon.core.network.PacketHandler;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CropTrackerClient extends CropTrackerCommon {
    @Override
    public ICropData getCropDataForLocation(World world, int x, int y, int z) {
        WorldLocation location = getCropKey(world, x, y, z);
        ICropData data = crops.get(location);
        if (data == null) { //If the data is null, ask the server!?
            PacketHandler.sendToServer(new PacketCropRequest(world, x, y, z));
        }
        
        return data != null? data: new CropData(location);
    }

    public void updateClient(boolean isRemoval, WorldLocation location, ICropData data) {
        if (isRemoval) {
            crops.remove(location);
        } else crops.put(location, data);
    }
}
