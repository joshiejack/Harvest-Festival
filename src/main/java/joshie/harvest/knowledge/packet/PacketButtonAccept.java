package joshie.harvest.knowledge.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.core.Letter;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import joshie.harvest.core.network.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

@Packet(Side.SERVER)
public class PacketButtonAccept extends PenguinPacket {
    private Letter letter;

    public PacketButtonAccept() {}
    public PacketButtonAccept(Letter letter) {
        this.letter = letter;
    }

    @Override
    public void toBytes(ByteBuf to) {
        ByteBufUtils.writeUTF8String(to, letter.getResource().toString());
    }

    @Override
    public void fromBytes(ByteBuf from) {
        letter = Letter.REGISTRY.get(new ResourceLocation(ByteBufUtils.readUTF8String(from)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handlePacket(EntityPlayer player) {
        letter.onLetterAccepted(player);
        player.closeScreen();
    }
}
