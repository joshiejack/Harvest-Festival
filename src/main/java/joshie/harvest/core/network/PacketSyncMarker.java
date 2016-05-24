package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.blocks.tiles.TileMarker;
import joshie.harvest.buildings.Building;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketSyncMarker extends AbstractPacketLocation {
    private Building building;

    public PacketSyncMarker() {}
    public PacketSyncMarker(int dimension, BlockPos pos, Building group) {
        super(dimension, pos);
        this.building = group;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        if (building != null) {
            buf.writeBoolean(true);
            ByteBufUtils.writeUTF8String(buf, BuildingRegistry.REGISTRY.getNameForObject(building).toString());
        } else buf.writeBoolean(false);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        if (buf.readBoolean()) {
            building = BuildingRegistry.REGISTRY.getObject(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        TileEntity tile = MCClientHelper.getWorld().getTileEntity(pos);
        if (tile instanceof TileMarker) {
            ((TileMarker) tile).setBuilding(building);
        }

        MCClientHelper.refresh(dim, pos);
    }
}