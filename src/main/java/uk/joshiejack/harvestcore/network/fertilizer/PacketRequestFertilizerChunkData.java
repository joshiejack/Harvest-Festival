package uk.joshiejack.harvestcore.network.fertilizer;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.ticker.TickerHelper;
import uk.joshiejack.penguinlib.ticker.data.ChunkData;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.SERVER)
public class PacketRequestFertilizerChunkData extends PenguinPacket {
    private long chunk;

    public PacketRequestFertilizerChunkData(){}
    public PacketRequestFertilizerChunkData(long chunk) {
        this.chunk = chunk;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(chunk);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        chunk = buf.readLong();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        ChunkData data = TickerHelper.getChunkData(player.world.provider.getDimension(), chunk);
        if (data != null) {
            PenguinNetwork.sendToClient(new PacketSyncFertilizerChunkData(data.entries()), player);
        }
    }
}
