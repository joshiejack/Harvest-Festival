package joshie.harvest.quests.player.meetings;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestMeeting;
import net.minecraft.entity.player.EntityPlayer;

@HFQuest("meeting.candice")
public class QuestMeetCandice extends QuestMeeting {
    public QuestMeetCandice() {
        super(HFBuildings.SUPERMARKET, HFNPCs.MILKMAID);
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        rewardItem(player, HFCooking.MEAL.getStackFromEnum(Meal.MILK_HOT));
    }
}
