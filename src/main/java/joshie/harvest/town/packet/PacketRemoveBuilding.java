package joshie.harvest.town.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.buildings.Building;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import joshie.harvest.core.network.PenguinPacket;
import joshie.harvest.town.tracker.TownTrackerClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.UUID;

@Packet(Side.CLIENT)
public class PacketRemoveBuilding extends PenguinPacket {
    private UUID uuid;
    private Building building;

    @SuppressWarnings("unused")
    public PacketRemoveBuilding() {}
    public PacketRemoveBuilding(UUID uuid, Building building) {
        this.uuid = uuid;
        this.building = building;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, uuid.toString());
        ByteBufUtils.writeUTF8String(buf, building.getResource().toString());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        uuid = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        building = Building.REGISTRY.get(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        HFTrackers.<TownTrackerClient>getTowns(player.worldObj).getTownByID(uuid).removeBuilding(building);
    }
}
