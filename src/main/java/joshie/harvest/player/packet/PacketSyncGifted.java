package joshie.harvest.player.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.npc.NPCStatus;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.network.Packet;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

@Packet(Packet.Side.CLIENT)
public class PacketSyncGifted extends PacketRelationship {
    private boolean gifted;
    private NPCStatus status;

    public PacketSyncGifted() {}
    public PacketSyncGifted(UUID key, NPCStatus status, boolean gifted) {
        super(key, false);
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
        status = NPCStatus.values()[buf.readByte()];
    }

    @Override
    protected void handleRelationship(EntityPlayer player, UUID key) {
        HFTrackers.getClientPlayerTracker().getRelationships().setStatus(key, status, gifted);
    }
}