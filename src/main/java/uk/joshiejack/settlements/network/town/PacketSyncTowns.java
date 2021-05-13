package uk.joshiejack.settlements.network.town;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import uk.joshiejack.settlements.client.WorldMap;
import uk.joshiejack.settlements.client.town.TownClient;
import uk.joshiejack.settlements.network.town.people.PacketRequestCustomNPCS;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.settlements.world.town.TownServer;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Collection;
import java.util.Objects;

@PenguinLoader(side = Side.CLIENT)
public class PacketSyncTowns extends PenguinPacket {
    private Collection<Town> towns;
    private int dimension;

    public PacketSyncTowns() {}
    public PacketSyncTowns(int dimension, Collection<TownServer> towns) {
        this.dimension = dimension;
        this.towns = Lists.newArrayList(towns);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dimension);
        buf.writeInt(towns.size());
        towns.forEach(t-> {
                buf.writeInt(t.getID());
                buf.writeLong(t.getCentre().toLong());
                ByteBufUtils.writeTag(buf, t.getTagForSync());
        });
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dimension = buf.readInt();
        towns = Lists.newArrayList();
        int count = buf.readInt();
        for (int i = 0; i < count; i++) {
            TownClient town = new TownClient(buf.readInt(), BlockPos.fromLong(buf.readLong()));
            town.deserializeNBT(Objects.requireNonNull(ByteBufUtils.readTag(buf)));
            towns.add(town);
        }
    }


    @Override
    public void handlePacket(EntityPlayer player) {
        WorldMap.setTowns(dimension, towns);
        //Request the Custom NPCS from the server
        PenguinNetwork.sendToServer(new PacketRequestCustomNPCS(dimension));
    }
}
