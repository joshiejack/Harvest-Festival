package joshie.harvest.town.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import joshie.harvest.town.data.TownDataClient;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

@Packet(Side.CLIENT)
public class PacketSyncCentre extends PacketSyncTown {
    private BlockPos pos;

    @SuppressWarnings("unused")
    public PacketSyncCentre(){}
    public PacketSyncCentre(UUID town, BlockPos pos) {
        super(town);
        this.pos = pos;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeLong(pos.toLong());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void handlePacket(TownDataClient town) {
        town.setCentre(pos);
    }
}
