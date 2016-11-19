package joshie.harvest.player.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.npc.RelationStatus;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.network.Packet;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

@Packet(Packet.Side.CLIENT)
public class PacketSyncGifted extends PacketRelationship {
    private boolean gifted;
    private RelationStatus status;

    public PacketSyncGifted() {}
    public PacketSyncGifted(UUID key, RelationStatus status, boolean gifted) {
        super(key, null);
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
    protected void handleRelationship(EntityPlayer player, UUID key) {
        HFTrackers.getClientPlayerTracker().getRelationships().setStatus(key, status, gifted);
    }
}