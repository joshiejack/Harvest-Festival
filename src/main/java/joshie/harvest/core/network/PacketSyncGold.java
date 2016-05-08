package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.network.penguin.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;

public class PacketSyncGold extends PenguinPacket {
    private long gold;

    public PacketSyncGold() {
    }

    public PacketSyncGold(long gold) {
        this.gold = gold;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(gold);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        gold = buf.readLong();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        HFTrackers.getClientPlayerTracker().getStats().setGold(gold);
    }
}