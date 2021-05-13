package uk.joshiejack.penguinlib.network;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.penguinlib.inventory.ContainerBook;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.reflect.InvocationTargetException;

@PenguinLoader(side = Side.SERVER)
public class PacketSetBookInventory extends PenguinPacket {
    private byte id;

    public PacketSetBookInventory() {}
    public PacketSetBookInventory(int id) {
        this.id = (byte) id;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(id);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        id = buf.readByte();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        if (id == -1) player.openContainer = new ContainerBook(0);
        else {
            try {
                player.openContainer = ContainerBook.REGISTRY.get(id).getConstructor(EntityPlayer.class).newInstance(player);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
}
