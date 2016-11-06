package joshie.harvest.quests.player.meetings;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.base.QuestMeeting;
import net.minecraft.entity.player.EntityPlayer;

@HFQuest("meeting.katlin")
public class QuestMeetKatlin extends QuestMeeting {
    public QuestMeetKatlin() {
        super(HFBuildings.CAFE, HFNPCs.CAFE_GRANNY);
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        rewardGold(player, 300);
    }
}
