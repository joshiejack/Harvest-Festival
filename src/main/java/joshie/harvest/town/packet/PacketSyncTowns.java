package joshie.harvest.town.packet;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
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

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

@Packet(Side.CLIENT)
public class PacketSyncTowns extends PenguinPacket {
    private Collection<TownDataServer> servers;
    private Collection<TownDataClient> clients;
    private BiMap<UUID, Integer> ids;

    @SuppressWarnings("unused")
    public PacketSyncTowns(){}
    public PacketSyncTowns(Collection<TownDataServer> townData, BiMap<UUID, Integer> townIDs) {
        servers = townData;
        ids = townIDs;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NBTTagCompound tag = new NBTTagCompound();
        //Write the town data
        NBTTagList list = new NBTTagList();
        for (TownDataServer town: servers) {
            NBTTagCompound compound = new NBTTagCompound();
            town.writePacketNBT(compound);
            list.appendTag(compound);
        }

        tag.setTag("Towns", list);

        //Write the mine ids
        NBTTagList list2 = new NBTTagList();
        for (UUID uuid: ids.keySet()) {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setString("UUID", uuid.toString());
            compound.setInteger("ID", ids.get(uuid));
            list2.appendTag(compound);
        }

        tag.setTag("Mines", list2);
        ByteBufUtils.writeTag(buf, tag);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        clients = new HashSet<>();
        NBTTagCompound tag = ByteBufUtils.readTag(buf);
        //Read the town data
        NBTTagList list = tag.getTagList("Towns", 10);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound compound = list.getCompoundTagAt(i);
            TownDataClient town = new TownDataClient();
            town.readFromNBT(compound);
            clients.add(town);
        }

        //Read the mine ids
        ids = HashBiMap.create();
        NBTTagList list2 = tag.getTagList("Mines", 10);
        for (int i = 0; i < list2.tagCount(); i++) {
            NBTTagCompound compound = list2.getCompoundTagAt(i);
            UUID uuid = UUID.fromString(compound.getString("UUID"));
            int mine = compound.getInteger("ID");
            ids.put(uuid, mine);
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        HFTrackers.<TownTrackerClient>getTowns(player.world).setTowns(clients, ids);
    }
}
