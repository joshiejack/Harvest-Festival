package uk.joshiejack.harvestcore.network.fertilizer;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import uk.joshiejack.harvestcore.client.renderer.fertilizer.FertilizerRender;
import uk.joshiejack.harvestcore.registry.Fertilizer;
import uk.joshiejack.harvestcore.ticker.crop.SoilTicker;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.ticker.DailyTicker;
import uk.joshiejack.penguinlib.util.PenguinLoader;

@PenguinLoader(side = Side.CLIENT)
public class PacketSyncFertilizerChunkData extends PenguinPacket {
    private Long2ObjectMap<Fertilizer> entries;

    public PacketSyncFertilizerChunkData(){}
    public PacketSyncFertilizerChunkData(Long2ObjectMap<DailyTicker> entries) {
        this.entries = new Long2ObjectOpenHashMap<>();
        entries.forEach((pos, entry) -> {
            if (entry.getClass() == SoilTicker.class) {
                Fertilizer type = ((SoilTicker) entry).getFertilizer();
                if (!type.equals(Fertilizer.NONE)) {
                    this.entries.put(pos, type);
                }
            }
        });
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entries.size());
        entries.forEach((k, v) -> {
            buf.writeLong(k);
            ByteBufUtils.writeUTF8String(buf, v.getRegistryName().toString());
        });
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entries = new Long2ObjectOpenHashMap<>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            long pos = buf.readLong();
            Fertilizer type = Fertilizer.REGISTRY.get(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
            entries.put(pos, type);
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        entries.forEach((k, v) -> FertilizerRender.addBlock(v, BlockPos.fromLong(k)));

        FertilizerRender.CACHE.invalidateAll();
    }
}
