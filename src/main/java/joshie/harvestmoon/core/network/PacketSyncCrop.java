package joshie.harvestmoon.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvestmoon.core.helpers.ClientHelper;
import joshie.harvestmoon.core.helpers.generic.MCClientHelper;
import joshie.harvestmoon.crops.CropData;
import joshie.harvestmoon.crops.WorldLocation;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncCrop implements IMessage, IMessageHandler<PacketSyncCrop, IMessage> {
    private boolean isRemoval;
    private WorldLocation location;
    private CropData data;

    public PacketSyncCrop() {}

    public PacketSyncCrop(WorldLocation location, CropData data) {
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
        ClientHelper.getCropTracker().sync(msg.isRemoval, msg.location, msg.data);
        MCClientHelper.refresh(msg.location.dimension, msg.location.x, msg.location.y, msg.location.z);

        return null;
    }
}