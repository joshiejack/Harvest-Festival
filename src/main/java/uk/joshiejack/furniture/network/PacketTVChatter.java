package uk.joshiejack.furniture.network;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.furniture.client.gui.GuiTVChatter;
import uk.joshiejack.penguinlib.network.packet.PacketChatter;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@PenguinLoader(side = Side.CLIENT)
public class PacketTVChatter extends PacketChatter {
    private BlockPos pos;

    public PacketTVChatter() {}
    public PacketTVChatter(String text, BlockPos pos) {
        super(text);
        this.pos = pos;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeLong(pos.toLong());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        pos = BlockPos.fromLong(buf.readLong());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void handlePacket(EntityPlayer player) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiTVChatter(pos, text));
    }
}

