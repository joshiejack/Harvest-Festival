package joshie.harvest.quests.player.meetings;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestMeeting;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Set;

import static joshie.harvest.quests.Quests.ASHLEE_MEET;
import static joshie.harvest.quests.Quests.JIM_MEET;

@HFQuest("meeting.jacob")
public class QuestMeetJacob extends QuestMeeting {
    public QuestMeetJacob() {
        super(HFBuildings.FISHING_HUT, HFNPCs.FISHERMAN);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(ASHLEE_MEET) && finished.contains(JIM_MEET);
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        HFApi.quests.completeQuestConditionally(Quests.BUILDING_FISHER, player);
        rewardItem(player, HFFishing.FISHING_ROD.getStack(ToolTier.BASIC));
    }
}
