package joshie.harvest.quests.packet;

import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import joshie.harvest.core.network.PenguinPacket;
import joshie.harvest.quests.data.QuestData;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.player.EntityPlayer;

import static joshie.harvest.town.TownHelper.getClosestTownToEntity;

@Packet(Side.SERVER)
public class PacketQuestStart extends PenguinPacket {
    public PacketQuestStart() {}

    @Override
    public void handlePacket(EntityPlayer player) {
        QuestData data = TownHelper.getClosestTownToEntity(player).getQuests();
        Quest quest = getClosestTownToEntity(player).getDailyQuest();
        if (quest != null && !data.getCurrent().contains(quest)) {
            data.startQuest(quest, true);
        }
    }
}