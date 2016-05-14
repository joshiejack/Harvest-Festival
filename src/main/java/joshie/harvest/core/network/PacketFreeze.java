package joshie.harvest.core.network;

import joshie.harvest.core.config.NPC;
import joshie.harvest.core.network.penguin.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;

public class PacketFreeze extends PenguinPacket {
    public PacketFreeze() {}

    @Override
    public void handlePacket(EntityPlayer player) {
        NPC.freezeNPC = true;
    }
}