package uk.joshiejack.penguinlib.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;

@PenguinLoader
public class PacketOpenUniversalGuide extends PenguinPacket {
    private int hand;

    public PacketOpenUniversalGuide(){}

    public PacketOpenUniversalGuide setHand(int hand) {
        this.hand = hand;
        return this;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(hand);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        hand = buf.readInt();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        player.openGui(PenguinLib.instance, 0, player.world, hand, 0, 0);
        if (player.world.isRemote) {
            //Send back the confirmation packet
            PenguinNetwork.sendToServer(new PacketOpenUniversalGuide().setHand(hand));
        }
    }
}
