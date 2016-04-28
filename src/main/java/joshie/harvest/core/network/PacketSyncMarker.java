package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.WorldLocation;
import joshie.harvest.blocks.tiles.TileMarker;
import joshie.harvest.buildings.Building;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncMarker implements IMessage, IMessageHandler<PacketSyncMarker, IMessage> {
    private WorldLocation location;
    private Building group;

    public PacketSyncMarker() {
    }

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
        TileEntity tile = MCClientHelper.getWorld().getTileEntity(msg.location.position);
        if (tile instanceof TileMarker) {
            ((TileMarker) tile).setBuilding(msg.group);
        }

        MCClientHelper.refresh(msg.location.dimension, msg.location.position);
        return null;
    }
}