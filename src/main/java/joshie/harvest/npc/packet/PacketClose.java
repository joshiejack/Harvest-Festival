package joshie.harvest.npc.packet;

import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import joshie.harvest.core.network.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;

@Packet(Side.SERVER)
public class PacketClose extends PenguinPacket {
    public PacketClose() {}

    @Override
    public void handlePacket(EntityPlayer player) {
        player.closeScreen();
    }
}
