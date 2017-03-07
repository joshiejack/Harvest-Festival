package joshie.harvest.quests.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import joshie.harvest.core.network.PenguinPacket;
import joshie.harvest.quests.town.festivals.contest.QuestContest;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownDataClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

@Packet(Side.CLIENT)
public class PacketQuestUpdateNames extends PenguinPacket {
    private List<Pair<String, Integer>> list;

    @SuppressWarnings("unused")
    public PacketQuestUpdateNames() {}
    public PacketQuestUpdateNames(List<Pair<String, Integer>> list) {
        this.list = list;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(list.size());
        for (Pair<String, Integer> pair : list) {
            ByteBufUtils.writeUTF8String(buf, pair.getKey());
            buf.writeInt(pair.getValue());
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int amount = buf.readInt();
        list = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            String name = ByteBufUtils.readUTF8String(buf);
            int stall = buf.readInt();
            list.add(Pair.of(name, stall));
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        TownDataClient town = TownHelper.getClosestTownToEntity(player, false);
        QuestContest quest = town.getQuests().getAQuest(town.getFestival().getQuest());
        if (quest != null) {
            quest.getEntries().setEntryNames(list);
        }
    }
}
