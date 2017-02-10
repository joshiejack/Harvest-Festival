package joshie.harvest.quests.player.meetings;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestMeeting;

import java.util.Set;

import static joshie.harvest.quests.Quests.*;

@HFQuest("meeting.tomas")
public class QuestMeetTomas extends QuestMeeting {
    public QuestMeetTomas() {
        super(HFBuildings.CHURCH, HFNPCs.PRIEST);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(JADE_MEET) && finished.contains(YULIF_MEET);
    }
}
