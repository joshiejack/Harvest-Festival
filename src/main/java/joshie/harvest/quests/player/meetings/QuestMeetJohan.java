package joshie.harvest.quests.player.meetings;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestMeeting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@HFQuest("meeting.johan")
public class QuestMeetJohan extends QuestMeeting {
    public QuestMeetJohan() {
        super(HFBuildings.FESTIVAL_GROUNDS, HFNPCs.TRADER);
    }

    @Override
    public String getDescription(World world, EntityPlayer player) {
        if (building.getRules().canDo(world, player, 1)) {
            return hasBuilding(player) ? getLocalized("description") : getLocalized("build");
        } else return null;
    }

    @Override
    public ItemStack getCurrentIcon(World world, EntityPlayer player) {
        return hasBuilding(player) ? primary : buildingStack;
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        HFApi.quests.completeQuestConditionally(Quests.BUILDING_FESTIVALS, player);
    }
}
