package joshie.harvest.player.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.network.Packet;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

@Packet(Packet.Side.CLIENT)
public class PacketSyncGifted extends PacketRelationship {
    private boolean gifted;

    public PacketSyncGifted() {}
    public PacketSyncGifted(UUID key, boolean gifted) {
        super(key, false);
        this.gifted = gifted;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(gifted);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        gifted = buf.readBoolean();
    }

    @Override
    protected void handleRelationship(EntityPlayer player, UUID key) {
        HFTrackers.getClientPlayerTracker().getRelationships().setGifted(key, gifted);
    }
}