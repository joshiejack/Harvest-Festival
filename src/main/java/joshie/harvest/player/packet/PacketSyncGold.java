package joshie.harvest.player.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;

@Packet(Packet.Side.CLIENT)
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