package joshie.harvest.knowledge.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.core.Letter;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import joshie.harvest.knowledge.letter.LetterDataClient;
import joshie.harvest.quests.packet.PacketSharedSync;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

@Packet(Side.CLIENT)
public class PacketAddLetter extends PacketSharedSync {
    private ResourceLocation letter;

    public PacketAddLetter(){}
    public PacketAddLetter(Letter letter) {
        this.letter = letter.getResource();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        ByteBufUtils.writeUTF8String(buf, letter.toString());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        letter = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        this.<LetterDataClient>getLetterDataFromPlayer(player).add(letter);
    }
}
