package uk.joshiejack.harvestcore.network.mail;

import uk.joshiejack.harvestcore.client.mail.Inbox;
import uk.joshiejack.harvestcore.registry.letter.Letter;
import uk.joshiejack.penguinlib.network.packet.PacketSendPenguin;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.CLIENT)
public class PacketReceiveLetter extends PacketSendPenguin<Letter> {
    public PacketReceiveLetter() {
        super(Letter.REGISTRY);
    }

    public PacketReceiveLetter(Letter letter) {
        super(Letter.REGISTRY, letter);
    }

    @Override
    public void handlePacket(EntityPlayer player, Letter letter) {
        Inbox.PLAYER.remove(letter);
    }
}