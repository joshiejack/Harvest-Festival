package uk.joshiejack.harvestcore.network.mail;

import uk.joshiejack.harvestcore.client.mail.Inbox;
import uk.joshiejack.harvestcore.registry.letter.Letter;
import uk.joshiejack.penguinlib.network.packet.PacketSendPenguinList;
import uk.joshiejack.penguinlib.util.PenguinGroup;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

@PenguinLoader(side = Side.CLIENT)
public class PacketSyncLetters extends PacketSendPenguinList<Letter> {
    public PacketSyncLetters() {
        super(Letter.REGISTRY);
    }

    public PacketSyncLetters(List<Letter> letter) {
        super(Letter.REGISTRY, letter);
    }

    @Override
    public void handlePacket(EntityPlayer player, List<Letter> letters) {
        Inbox.PLAYER.set(letters, PenguinGroup.PLAYER, PenguinGroup.TEAM, PenguinGroup.GLOBAL);
    }
}