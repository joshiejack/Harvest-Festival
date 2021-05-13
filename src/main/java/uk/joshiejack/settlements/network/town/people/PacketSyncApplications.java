package uk.joshiejack.settlements.network.town.people;

import com.google.common.collect.Sets;
import io.netty.buffer.ByteBuf;
import uk.joshiejack.settlements.client.WorldMap;
import uk.joshiejack.settlements.world.town.people.Government;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

@PenguinLoader(side = Side.CLIENT)
public class PacketSyncApplications extends PenguinPacket {
    private final Set<UUID> applications = Sets.newHashSet();
    private int dimension, id;

    public PacketSyncApplications() {}
    public PacketSyncApplications(int dimension, int id, Set<UUID> applications) {
        this.dimension = dimension;
        this.id = id;
        this.applications.addAll(applications);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dimension);
        buf.writeInt(id);
        buf.writeByte(applications.size());
        applications.forEach(a -> ByteBufUtils.writeUTF8String(buf, a.toString()));
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dimension = buf.readInt();
        id = buf.readInt();
        int size = buf.readByte();
        IntStream.range(0, size).forEach(a -> applications.add(UUID.fromString(ByteBufUtils.readUTF8String(buf))));
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        Government government = WorldMap.getTownByID(dimension, id).getGovernment();
        government.getApplications().clear();
        government.getApplications().addAll(applications);
        GuiScreen screen = Minecraft.getMinecraft().currentScreen;
        if (screen instanceof GuiBook) {
            ((GuiBook)screen).setPage(((GuiBook)screen).getPage());
        }
    }
}
