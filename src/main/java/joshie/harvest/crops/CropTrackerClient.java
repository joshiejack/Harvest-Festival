package joshie.harvest.crops;

import joshie.harvest.api.crops.ICropData;
import joshie.harvest.core.network.PacketCropRequest;
import joshie.harvest.core.network.PacketHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CropTrackerClient extends CropTracker {
    @Override
    public ICropData getCropDataForLocation(BlockPos pos) {
        ICropData data = super.getCropDataForLocation(pos);
        if (data == null || data.getCrop() == HFCrops.NULL_CROP) { //If the data is null, ask the server!?
            PacketHandler.sendToServer(new PacketCropRequest(getDimension(), pos));
        }

        return data != null ? data : new CropData(pos);
    }

    public void updateClient(BlockPos position, ICropData data, boolean isRemoval) {
        if (isRemoval) {
            this.cropTracker.remove(position);
        } else this.cropTracker.put(position, data);
    }
}