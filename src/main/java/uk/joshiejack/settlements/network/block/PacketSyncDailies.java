package uk.joshiejack.settlements.network.block;

import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.tuple.Pair;
import uk.joshiejack.settlements.quest.Quest;
import uk.joshiejack.settlements.quest.settings.QuestBoardClient;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import java.util.Map;

@PenguinLoader(side = Side.CLIENT)
public class PacketSyncDailies extends PenguinPacket {
    private Map<ResourceLocation, Pair<String, String>> dailies;

    public PacketSyncDailies() { }
    public PacketSyncDailies(Map<Quest, Pair<String, String>> dailies) {
        this.dailies = Maps.newHashMap();
        dailies.forEach((quest, string) -> this.dailies.put(quest.getRegistryName(), string));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(dailies.size());
        for (Map.Entry<ResourceLocation, Pair<String, String>> script : dailies.entrySet()) {
            ByteBufUtils.writeUTF8String(buf, script.getKey().toString());
            ByteBufUtils.writeUTF8String(buf, script.getValue().getLeft());
            ByteBufUtils.writeUTF8String(buf, script.getValue().getRight());
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dailies = Maps.newHashMap();
        int count = buf.readByte();
        for (int i = 0; i < count; i++) {
            dailies.put(new ResourceLocation(ByteBufUtils.readUTF8String(buf)),
                    Pair.of(ByteBufUtils.readUTF8String(buf), ByteBufUtils.readUTF8String(buf)));
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        QuestBoardClient.setDailies(dailies);
    }
}
