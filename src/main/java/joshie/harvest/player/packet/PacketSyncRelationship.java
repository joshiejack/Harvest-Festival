package joshie.harvest.player.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.network.Packet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;

import java.util.UUID;

@Packet(Packet.Side.CLIENT)
public class PacketSyncRelationship extends PacketRelationship {
    private int value;

    public PacketSyncRelationship() {}
    public PacketSyncRelationship(UUID uuid, int value, EnumParticleTypes particles) {
        super(uuid, particles);
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
    protected void handleRelationship(EntityPlayer player, UUID key) {
        HFTrackers.getClientPlayerTracker().getRelationships().setRelationship(key, value);
    }
}