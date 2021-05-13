package uk.joshiejack.harvestcore.network.mail;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.harvestcore.registry.letter.Letter;
import uk.joshiejack.harvestcore.world.storage.SavedData;
import uk.joshiejack.penguinlib.network.packet.PacketSendPenguin;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.SERVER)
public class PacketLetterButtonClick extends PacketSendPenguin<Letter> {
    private boolean accept;

    public PacketLetterButtonClick() {
        super(Letter.REGISTRY);
    }
    public PacketLetterButtonClick(Letter letter, boolean accept) {
        super(Letter.REGISTRY, letter);
        this.accept = accept;
    }

    @Override
    public void toBytes(ByteBuf to) {
        to.writeBoolean(accept);
        super.toBytes(to);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        accept = from.readBoolean();
        super.fromBytes(from);
    }

    @Override
    public void handlePacket(EntityPlayer player, Letter letter) {
        if (accept) letter.accept(player);
        else letter.reject(player);
        SavedData.getMailroom(player.world, PlayerHelper.getUUIDForPlayer(player)).removeLetter(player.world, letter);
    }
}