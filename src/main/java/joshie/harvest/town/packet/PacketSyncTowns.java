package joshie.harvest.town.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import joshie.harvest.core.network.PenguinPacket;
import joshie.harvest.town.data.TownDataClient;
import joshie.harvest.town.data.TownDataServer;
import joshie.harvest.town.tracker.TownTrackerClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.HashSet;
import java.util.Set;

@Packet(Side.CLIENT)
public class PacketSyncTowns extends PenguinPacket {
    private Set<TownDataServer> servers;
    private Set<TownDataClient> clients;

    @SuppressWarnings("unused")
    public PacketSyncTowns(){}
    public PacketSyncTowns(Set<TownDataServer> townData) {
        servers = townData;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        for (TownDataServer town: servers) {
            NBTTagCompound compound = new NBTTagCompound();
            town.writePacketNBT(compound);
            list.appendTag(compound);
        }

        tag.setTag("Towns", list);
        ByteBufUtils.writeTag(buf, tag);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        clients = new HashSet<>();
        NBTTagCompound tag = ByteBufUtils.readTag(buf);
        NBTTagList list = tag.getTagList("Towns", 10);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound compound = list.getCompoundTagAt(i);
            TownDataClient town = new TownDataClient();
            town.readFromNBT(compound);
            clients.add(town);
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        HFTrackers.<TownTrackerClient>getTowns(player.worldObj).setTowns(clients);
    }
}
