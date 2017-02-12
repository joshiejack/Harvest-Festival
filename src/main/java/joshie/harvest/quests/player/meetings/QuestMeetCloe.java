package joshie.harvest.quests.player.meetings;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestMeeting;

@HFQuest("meeting.cloe")
public class QuestMeetCloe extends QuestMeeting {
    public QuestMeetCloe() {
        super(HFBuildings.TOWNHALL, HFNPCs.DAUGHTER_ADULT);
    }
}
