package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.WorldLocation;
import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.blocks.tiles.TileMarker;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.network.penguin.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketSyncMarker extends PenguinPacket {
    private WorldLocation location;
    private IBuilding group;

    public PacketSyncMarker() {}

    public PacketSyncMarker(WorldLocation location, IBuilding group) {
        this.location = location;
        this.group = group;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        location.toBytes(buf);
        if (group != null) {
            buf.writeBoolean(true);
            ByteBufUtils.writeUTF8String(buf, group.getResource().toString());
        } else buf.writeBoolean(false);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        location = new WorldLocation();
        location.fromBytes(buf);
        if (buf.readBoolean()) {
            group = HFApi.BUILDINGS.getBuildingFromName(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        TileEntity tile = MCClientHelper.getWorld().getTileEntity(location.position);
        if (tile instanceof TileMarker) {
            ((TileMarker) tile).setBuilding(group);
        }

        MCClientHelper.refresh(location.dimension, location.position);
    }
}