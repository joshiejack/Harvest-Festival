package joshie.harvest.quests.player.meetings;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestMeeting;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

@HFQuest("meeting.jacob")
public class QuestMeetJacob extends QuestMeeting {
    public QuestMeetJacob() {
        super(HFBuildings.FISHING_HUT, HFNPCs.FISHERMAN);
    }

    @Override
    public String getDescription(World world, EntityPlayer player) {
        if (hasBuilding(player)) return getLocalized("description");
        else if (TownHelper.getClosestTownToEntity(player, false).hasBuildings(building.getRequirements())) return getLocalized("build");
        else return null;
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        HFApi.quests.completeQuestConditionally(Quests.BUILDING_FISHER, player);
        rewardItem(player, HFFishing.FISHING_ROD.getStack(ToolTier.BASIC));
    }
}
