package joshie.harvest.quests.player.meetings;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestMeeting;

import java.util.Set;

import static joshie.harvest.quests.Quests.*;

@HFQuest("meeting.fenn")
public class QuestMeetFenn extends QuestMeeting {
    public QuestMeetFenn() {
        super(HFBuildings.CLOCKMAKER, HFNPCs.CLOCKMAKER_CHILD);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(JOHAN_MEET);
    }
}
