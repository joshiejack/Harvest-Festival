package joshie.harvest.player.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.RelationStatus;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.network.Packet;
import net.minecraft.entity.player.EntityPlayer;

@Packet(Packet.Side.CLIENT)
public class PacketSyncGifted extends PacketRelationship {
    private boolean gifted;
    private RelationStatus status;

    @SuppressWarnings("unused")
    public PacketSyncGifted() {}
    public PacketSyncGifted(NPC npc, RelationStatus status, boolean gifted) {
        super(npc);
        this.gifted = gifted;
        this.status = status;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(gifted);
        buf.writeByte(status.ordinal());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        gifted = buf.readBoolean();
        status = RelationStatus.values()[buf.readByte()];
    }

    @Override
    protected void handleRelationship(EntityPlayer player, NPC npc) {
        HFTrackers.getClientPlayerTracker().getRelationships().setStatus(npc, status, gifted);
    }
}