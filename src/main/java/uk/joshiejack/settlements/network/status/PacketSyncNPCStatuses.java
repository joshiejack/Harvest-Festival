package uk.joshiejack.settlements.network.status;

import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import uk.joshiejack.settlements.npcs.status.Statuses;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import java.util.Map;

@PenguinLoader(side = Side.CLIENT)
public class PacketSyncNPCStatuses extends PenguinPacket {
    private Map<ResourceLocation, Object2IntMap<String>> status;

    public PacketSyncNPCStatuses() {}
    public PacketSyncNPCStatuses(Map<ResourceLocation, Object2IntMap<String>> status) {
        this.status = status;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(status.size());
        for (Map.Entry<ResourceLocation, Object2IntMap<String>> e: status.entrySet()) {
            ByteBufUtils.writeUTF8String(buf, e.getKey().toString());
            buf.writeByte(e.getValue().size());
            for (String s: e.getValue().keySet()) {
                ByteBufUtils.writeUTF8String(buf, s);
                buf.writeInt(e.getValue().get(s));
            }
        }
    }

    private Object2IntMap<String> getOrCreateMap(ResourceLocation npc) {
        if (!status.containsKey(npc)) {
            status.put(npc, new Object2IntOpenHashMap<>());
        }

        return status.get(npc);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        status = Maps.newHashMap();
        int statusSize = buf.readByte();
        for (int i = 0; i < statusSize; i++) {
            ResourceLocation id = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
            int tObjectSize = buf.readByte();
            for (int j = 0; j < tObjectSize; j++) {
                String status = ByteBufUtils.readUTF8String(buf);
                int value = buf.readInt();
                getOrCreateMap(id).put(status, value);
            }
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        Statuses.setData(status);
    }
}
