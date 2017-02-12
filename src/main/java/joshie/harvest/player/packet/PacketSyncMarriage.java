package joshie.harvest.player.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.RelationStatus;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.network.Packet;
import net.minecraft.entity.player.EntityPlayer;

@Packet(Packet.Side.CLIENT)
@SuppressWarnings("unused")
public class PacketSyncMarriage extends PacketRelationship {
    private boolean divorce;

    public PacketSyncMarriage() {}
    public PacketSyncMarriage(NPC npc, boolean divorce) {
        super(npc);
        this.divorce = divorce;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(divorce);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        divorce = buf.readBoolean();
    }

    @Override
    protected void handleRelationship(EntityPlayer player, NPC npc) {
        HFTrackers.getClientPlayerTracker().getRelationships().setStatus(npc, RelationStatus.MARRIED, divorce);
    }
}