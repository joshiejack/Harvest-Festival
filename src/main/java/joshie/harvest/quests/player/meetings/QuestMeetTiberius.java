package joshie.harvest.quests.player.meetings;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestMeeting;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@HFQuest("meeting.tiberius")
public class QuestMeetTiberius extends QuestMeeting {
    public QuestMeetTiberius() {
        super(HFBuildings.CLOCKMAKER, HFNPCs.CLOCKMAKER);
    }

    @Override
    public String getDescription(World world, EntityPlayer player) {
        if (hasBuilding(player)) return getLocalized("description");
        else if (TownHelper.getClosestTownToEntity(player).hasBuildings(building.getRequirements())) return getLocalized("build");
        else return null;
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        rewardItem(player, new ItemStack(Items.CLOCK));
    }
}
