package uk.joshiejack.settlements.network.status;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.settlements.npcs.status.Statuses;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.CLIENT)
public class PacketNPCStatusUpdate extends PenguinPacket {
    private ResourceLocation npc;
    private String status;
    private int value;

    public PacketNPCStatusUpdate() {}
    public PacketNPCStatusUpdate(ResourceLocation npc, String status, int value) {
        this.npc = npc;
        this.status = status;
        this.value = value;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, npc.toString());
        ByteBufUtils.writeUTF8String(buf, status);
        buf.writeInt(value);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        npc = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
        status = ByteBufUtils.readUTF8String(buf);
        value = buf.readInt();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        Statuses.setStatus(npc, status, value);
    }
}
