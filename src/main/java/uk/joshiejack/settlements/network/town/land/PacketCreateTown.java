package uk.joshiejack.settlements.network.town.land;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.settlements.client.WorldMap;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.settlements.client.town.TownClient;
import uk.joshiejack.settlements.world.town.TownServer;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Objects;

@PenguinLoader(side = Side.CLIENT)
public class PacketCreateTown extends PenguinPacket {
    private int dimension;
    private Town<?> town;

    public PacketCreateTown() {}
    public PacketCreateTown(int dimension, TownServer town) {
        this.dimension = dimension;
        this.town = town;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dimension);
        buf.writeInt(town.getID());
        buf.writeLong(town.getCentre().toLong());
        ByteBufUtils.writeTag(buf, town.getTagForSync());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dimension = buf.readInt();
        town = new TownClient(buf.readInt(), BlockPos.fromLong(buf.readLong()));
        town.deserializeNBT(Objects.requireNonNull(ByteBufUtils.readTag(buf)));
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        WorldMap.addTown(dimension, (TownClient) town);
    }
}
