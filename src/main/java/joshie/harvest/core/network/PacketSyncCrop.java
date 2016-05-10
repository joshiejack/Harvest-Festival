package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.crops.ICropData;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.crops.CropData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class PacketSyncCrop extends AbstractPacketLocation {
    private boolean isRemoval;
    private ICropData data;

    public PacketSyncCrop() { }
    public PacketSyncCrop (int dimension, BlockPos pos, ICropData data) {
        super(dimension, pos);
        this.isRemoval = false;
        this.data = data;
    }

    public PacketSyncCrop(int dimension, BlockPos pos) {
        super(dimension, pos);
        this.isRemoval = true;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(isRemoval);
        if (!isRemoval) {
            data.toBytes(buf);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        isRemoval = buf.readBoolean();
        if (!isRemoval) {
            data = new CropData(pos, dim);
            data.fromBytes(buf);
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        HFTrackers.getCropTracker().updateClient(dim, pos, data, isRemoval);
        MCClientHelper.refresh(dim, pos);
    }
}