package joshie.harvest.crops;

import joshie.harvest.api.WorldLocation;
import joshie.harvest.api.crops.ICropData;
import joshie.harvest.core.network.PacketCropRequest;
import joshie.harvest.core.network.PacketHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CropTrackerClient extends CropTracker {
    @Override
    public ICropData getCropDataForLocation(World world, BlockPos pos) {
        WorldLocation location = getCropKey(world, pos);
        ICropData data = crops.get(location);
        if (data == null) { //If the data is null, ask the server!?
            PacketHandler.sendToServer(new PacketCropRequest(world, pos));
        }

        return data != null ? data : new CropData(location);
    }

    public void updateClient(boolean isRemoval, WorldLocation location, ICropData data) {
        if (isRemoval) {
            crops.remove(location);
        } else crops.put(location, data);
    }
}