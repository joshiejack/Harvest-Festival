package joshie.harvestmoon.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvestmoon.api.WorldLocation;
import joshie.harvestmoon.blocks.tiles.TileMarker;
import joshie.harvestmoon.buildings.Building;
import joshie.harvestmoon.core.helpers.generic.MCClientHelper;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncMarker implements IMessage, IMessageHandler<PacketSyncMarker, IMessage> {
    private WorldLocation location;
    private Building group;

    public PacketSyncMarker() {}

    public PacketSyncMarker(WorldLocation location, Building group) {
        this.location = location;
        this.group = group;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        location.toBytes(buf);
        ByteBufUtils.writeUTF8String(buf, group.getName());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        location = new WorldLocation();
        location.fromBytes(buf);
        group = Building.getGroup(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public IMessage onMessage(PacketSyncMarker msg, MessageContext ctx) {
        TileEntity tile = MCClientHelper.getWorld().getTileEntity(msg.location.x, msg.location.y, msg.location.z);
        if (tile instanceof TileMarker) {
            ((TileMarker) tile).setBuilding(msg.group);
        }

        MCClientHelper.refresh(msg.location.dimension, msg.location.x, msg.location.y, msg.location.z);

        return null;
    }
}