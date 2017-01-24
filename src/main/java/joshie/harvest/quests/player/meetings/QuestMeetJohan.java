package joshie.harvest.quests.player.meetings;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.festivals.HFFestivals;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestMeeting;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Set;

import static joshie.harvest.quests.Quests.*;

@HFQuest("meeting.johan")
public class QuestMeetJohan extends QuestMeeting {
    public QuestMeetJohan() {
        super(HFFestivals.FESTIVAL_GROUNDS, HFNPCs.TRADER);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(JENNI_MEET) || finished.contains(ASHLEE_MEET) || finished.contains(JIM_MEET);
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        HFApi.quests.completeQuestConditionally(Quests.BUILDING_FESTIVALS, player);
    }
}
