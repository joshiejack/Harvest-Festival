package joshie.harvest.quests.player.meetings;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestMeeting;

@HFQuest("meeting.fenn")
public class QuestMeetFenn extends QuestMeeting {
    public QuestMeetFenn() {
        super(HFBuildings.CLOCKMAKER, HFNPCs.CLOCKMAKER_CHILD);
    }
}
