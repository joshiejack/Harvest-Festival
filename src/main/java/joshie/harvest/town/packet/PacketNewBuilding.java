package joshie.harvest.town.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import joshie.harvest.core.network.PenguinPacket;
import joshie.harvest.town.TownBuilding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.UUID;

@Packet(Side.CLIENT)
public class PacketNewBuilding extends PenguinPacket {
    public PacketNewBuilding() {}
    private UUID uuid;
    private TownBuilding building;

    public PacketNewBuilding(UUID uuid, TownBuilding building) {
        this.uuid = uuid;
        this.building = building;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, uuid.toString());
        NBTTagCompound tag = new NBTTagCompound();
        building.writeToNBT(tag);
        ByteBufUtils.writeTag(buf, tag);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        uuid = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        building = new TownBuilding();
        building.readFromNBT(ByteBufUtils.readTag(buf));
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        HFTrackers.getTownTracker(player.worldObj).getTownByID(uuid).addBuilding(building);
    }
}
