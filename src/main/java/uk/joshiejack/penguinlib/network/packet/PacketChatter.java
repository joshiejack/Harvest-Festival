package uk.joshiejack.penguinlib.network.packet;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.penguinlib.client.gui.GuiChatter;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@PenguinLoader(side = Side.CLIENT)
public class PacketChatter extends PenguinPacket {
    protected String text;

    public PacketChatter() {}
    public PacketChatter(String text) {
        this.text = text;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, text);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        text = ByteBufUtils.readUTF8String(buf);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void handlePacket(EntityPlayer player) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiChatter(text));
    }
}

