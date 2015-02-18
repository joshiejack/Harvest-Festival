package joshie.harvestmoon.network;

import io.netty.buffer.ByteBuf;
import joshie.harvestmoon.blocks.tiles.TileMarker;
import joshie.harvestmoon.buildings.Building;
import joshie.harvestmoon.buildings.BuildingGroup;
import joshie.harvestmoon.crops.WorldLocation;
import joshie.harvestmoon.helpers.generic.MCClientHelper;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncMarker implements IMessage, IMessageHandler<PacketSyncMarker, IMessage> {
    private WorldLocation location;
    private BuildingGroup group;
    private Building building;

    public PacketSyncMarker() {}

    public PacketSyncMarker(WorldLocation location, BuildingGroup group, Building building) {
        this.location = location;
        this.group = group;
        this.building = building;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        location.toBytes(buf);
        ByteBufUtils.writeUTF8String(buf, group.getName());
        buf.writeInt(building.getInt());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        location = new WorldLocation();
        location.fromBytes(buf);
        group = BuildingGroup.getGroup(ByteBufUtils.readUTF8String(buf));
        building = group.getBuilding(buf.readInt());
    }

    @Override
    public IMessage onMessage(PacketSyncMarker msg, MessageContext ctx) {
        TileEntity tile = MCClientHelper.getWorld().getTileEntity(msg.location.x, msg.location.y, msg.location.z);
        if (tile instanceof TileMarker) {
            ((TileMarker) tile).setBuilding(msg.group, msg.building);
        }

        MCClientHelper.refresh(msg.location.dimension, msg.location.x, msg.location.y, msg.location.z);

        return null;
    }
}