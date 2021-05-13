package uk.joshiejack.settlements.network.town;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.client.WorldMap;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class PacketAbstractTownSync extends PenguinPacket {
    protected int dimension, id;

    public PacketAbstractTownSync(){}
    public PacketAbstractTownSync(int dimension, int town) {
        this.dimension = dimension;
        this.id = town;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dimension);
        buf.writeInt(id);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dimension = buf.readInt();
        id = buf.readInt();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void handleClientPacket(NetHandlerPlayClient handler) {
        handlePacket(Minecraft.getMinecraft().player, WorldMap.getTownByID(dimension, id));
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        handlePacket(player, AdventureDataLoader.get(player.world).getTownByID(dimension, id));
    }

    protected abstract void handlePacket(EntityPlayer player, Town<?> town);
}
