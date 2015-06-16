package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.WorldLocation;
import joshie.harvest.api.crops.ICropData;
import joshie.harvest.core.handlers.HFTracker;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.crops.CropData;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncCrop implements IMessage, IMessageHandler<PacketSyncCrop, IMessage> {
    private boolean isRemoval;
    private WorldLocation location;
    private ICropData data;

    public PacketSyncCrop() {}

    public PacketSyncCrop(WorldLocation location, ICropData data) {
        this.isRemoval = false;
        this.location = location;
        this.data = data;
    }

    public PacketSyncCrop(WorldLocation location) {
        this.isRemoval = true;
        this.location = location;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(isRemoval);
        location.toBytes(buf);
        if (!isRemoval) {
            data.toBytes(buf);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        isRemoval = buf.readBoolean();
        location = new WorldLocation();
        location.fromBytes(buf);
        if (!isRemoval) {
            data = new CropData(location);
            data.fromBytes(buf);
        }
    }

    @Override
    public IMessage onMessage(PacketSyncCrop msg, MessageContext ctx) {
        HFTracker.getCropTracker().updateClient(msg.isRemoval, msg.location, msg.data);
        MCClientHelper.refresh(msg.location.dimension, msg.location.x, msg.location.y, msg.location.z);

        return null;
    }
}