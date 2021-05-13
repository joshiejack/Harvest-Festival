package uk.joshiejack.penguinlib.network.packet;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.penguinlib.client.PenguinTeamsClient;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

@PenguinLoader(side = Side.CLIENT)
public class PacketChangeTeam extends PenguinPacket {
    private UUID player, oldTeam, newTeam;

    public PacketChangeTeam() {}
    public PacketChangeTeam(UUID player, UUID oldTeam, UUID newTeam) {
        this.player = player;
        this.oldTeam = oldTeam;
        this.newTeam = newTeam;
    }

    @Override
    public void toBytes(ByteBuf to) {
        ByteBufUtils.writeUTF8String(to, player.toString());
        ByteBufUtils.writeUTF8String(to, oldTeam.toString());
        ByteBufUtils.writeUTF8String(to, newTeam.toString());
    }

    @Override
    public void fromBytes(ByteBuf from) {
        player = UUID.fromString(ByteBufUtils.readUTF8String(from));
        oldTeam = UUID.fromString(ByteBufUtils.readUTF8String(from));
        newTeam = UUID.fromString(ByteBufUtils.readUTF8String(from));
    }

    @Override
    public void handlePacket(EntityPlayer unused) {
        PenguinTeamsClient.changeTeam(player, oldTeam, newTeam);
        GuiScreen screen = Minecraft.getMinecraft().currentScreen;
        if (screen instanceof GuiBook) {
            ((GuiBook)screen).setPage(((GuiBook)screen).getPage());
        }
    }
}
