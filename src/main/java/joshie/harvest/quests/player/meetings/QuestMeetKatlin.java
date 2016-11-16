package joshie.harvest.quests.player.meetings;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestMeeting;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Set;

import static joshie.harvest.quests.Quests.JENNI_MEET;

@HFQuest("meeting.katlin")
public class QuestMeetKatlin extends QuestMeeting {
    public QuestMeetKatlin() {
        super(HFBuildings.CAFE, HFNPCs.CAFE_GRANNY);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(JENNI_MEET);
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        HFApi.quests.completeQuestConditionally(Quests.BUILDING_CAFE, player);
        rewardGold(player, 300);
    }
}
