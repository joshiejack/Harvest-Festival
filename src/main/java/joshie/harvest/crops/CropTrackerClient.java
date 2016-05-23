package joshie.harvest.crops;

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
        ICropData data = super.getCropDataForLocation(world, pos);
        if (data == null || data.getCrop() == HFCrops.null_crop) { //If the data is null, ask the server!?
            PacketHandler.sendToServer(new PacketCropRequest(world, pos));
        }

        return data != null ? data : new CropData(pos);
    }

    public void updateClient(int dimension, BlockPos position, ICropData data, boolean isRemoval) {
        if (isRemoval) {
            this.cropTracker.remove(position);
        } else this.cropTracker.put(position, data);
    }
}