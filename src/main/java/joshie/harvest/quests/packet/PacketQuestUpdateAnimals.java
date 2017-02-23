package joshie.harvest.quests.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import joshie.harvest.core.network.PenguinPacket;
import joshie.harvest.quests.base.QuestAnimalContest;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownDataClient;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

@Packet(Side.CLIENT)
public class PacketQuestUpdateAnimals extends PenguinPacket {
    private List<Pair<String, Integer>> list;

    public PacketQuestUpdateAnimals() {}
    public PacketQuestUpdateAnimals(List<Pair<String, Integer>> list) {
        this.list = list;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(list.size());
        for (int i = 0; i < list.size(); i++) {
            Pair<String, Integer> pair = list.get(i);
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

    public static <E extends EntityAnimal> List<Pair<String, Integer>> getNamesFromEntities(List<Pair<E, Integer>> list) {
        List<Pair<String, Integer>> list1 = new ArrayList<>();
        for (Pair<E, Integer> pair: list) {
            list1.add(Pair.of(pair.getKey().getName(), pair.getValue()));
        }

        return list1;
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        TownDataClient town = TownHelper.getClosestTownToEntity(player, false);
        QuestAnimalContest quest = town.getQuests().getAQuest(town.getFestival().getQuest());
        if (quest != null) {
            quest.getEntries().setAnimalNames(list);
        }
    }
}
