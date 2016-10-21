package joshie.harvest.player.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.npc.NPCStatus;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.network.Packet;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

@Packet(Packet.Side.CLIENT)
public class PacketSyncMarriage extends PacketRelationship {
    private boolean divorce;

    public PacketSyncMarriage() {}
    public PacketSyncMarriage(UUID key, boolean divorce) {
        super(key, false);
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
    protected void handleRelationship(EntityPlayer player, UUID key) {
        HFTrackers.getClientPlayerTracker().getRelationships().setStatus(key, NPCStatus.MARRIED, divorce);
    }
}