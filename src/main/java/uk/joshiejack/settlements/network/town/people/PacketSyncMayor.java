package uk.joshiejack.settlements.network.town.people;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.settlements.client.WorldMap;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.CLIENT)
public class PacketSyncMayor extends PenguinPacket {
    private String mayor;
    private int dimension, id;

    public PacketSyncMayor() {}
    public PacketSyncMayor(int dimension, int id, String mayor) {
        this.dimension = dimension;
        this.id = id;
        this.mayor = mayor;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dimension);
        buf.writeInt(id);
        ByteBufUtils.writeUTF8String(buf, mayor);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dimension = buf.readInt();
        id = buf.readInt();
        mayor = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        Town<?> town = WorldMap.getTownByID(dimension, id);
        if (town != null) {
            town.getCharter().setMayorString(mayor);
        }
    }
}
