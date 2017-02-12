package joshie.harvest.quests.player.meetings;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestMeeting;
import net.minecraft.entity.player.EntityPlayer;

@HFQuest("meeting.katlin")
public class QuestMeetKatlin extends QuestMeeting {
    public QuestMeetKatlin() {
        super(HFBuildings.CAFE, HFNPCs.CAFE_GRANNY);
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        HFApi.quests.completeQuestConditionally(Quests.BUILDING_CAFE, player);
        rewardGold(player, 300);
    }
}
