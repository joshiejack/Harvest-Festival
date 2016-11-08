package joshie.harvest.quests.player.meetings;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestMeeting;
import net.minecraft.entity.player.EntityPlayer;

@HFQuest("meeting.johan")
public class QuestMeetJohan extends QuestMeeting {
    public QuestMeetJohan() {
        super(HFBuildings.FESTIVALS, HFNPCs.TRADER);
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        HFApi.quests.completeQuestConditionally(Quests.BUILDING_FESTIVALS, player);
    }
}
