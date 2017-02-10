package joshie.harvest.quests.player.meetings;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestMeeting;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Set;

import static joshie.harvest.quests.Quests.JADE_MEET;

@HFQuest("meeting.candice")
public class QuestMeetCandice extends QuestMeeting {
    public QuestMeetCandice() {
        super(HFBuildings.SUPERMARKET, HFNPCs.MILKMAID);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(JADE_MEET);
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        HFApi.quests.completeQuestConditionally(Quests.BUILDING_SUPERMARKET, player);
        rewardItem(player, HFCooking.MEAL.getStackFromEnum(Meal.MILK_HOT));
    }
}
