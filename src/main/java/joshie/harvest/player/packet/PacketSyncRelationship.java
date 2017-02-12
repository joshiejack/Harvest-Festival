package joshie.harvest.player.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.network.Packet;
import net.minecraft.entity.player.EntityPlayer;

@Packet(Packet.Side.CLIENT)
public class PacketSyncRelationship extends PacketRelationship {
    private int value;

    @SuppressWarnings("unused")
    public PacketSyncRelationship() {}
    public PacketSyncRelationship(NPC npc, int value) {
        super(npc);
        this.value = value;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(value);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        value = buf.readInt();
    }

    @Override
    protected void handleRelationship(EntityPlayer player, NPC npc) {
        HFTrackers.getClientPlayerTracker().getRelationships().setRelationship(npc, value);
    }
}