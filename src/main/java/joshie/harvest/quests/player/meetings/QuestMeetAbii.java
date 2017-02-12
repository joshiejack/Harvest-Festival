package joshie.harvest.quests.player.meetings;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestMeeting;

@HFQuest("meeting.abi")
public class QuestMeetAbii extends QuestMeeting {
    public QuestMeetAbii() {
        super(HFBuildings.TOWNHALL, HFNPCs.DAUGHTER_CHILD);
    }
}
