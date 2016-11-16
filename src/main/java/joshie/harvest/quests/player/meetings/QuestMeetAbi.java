package joshie.harvest.quests.player.meetings;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.base.QuestMeeting;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Set;

import static joshie.harvest.quests.Quests.*;

@HFQuest("meeting.abi")
public class QuestMeetAbi extends QuestMeeting {
    public QuestMeetAbi() {
        super(HFBuildings.TOWNHALL, HFNPCs.DAUGHTER_CHILD);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(JENNI_MEET) && finished.contains(ASHLEE_MEET) && finished.contains(JIM_MEET);
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        rewardItem(player, HFCooking.MEAL.getStackFromEnum(Meal.COOKIES, 3));
    }
}
