package joshie.harvest.quests.town.seeds;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.quests.base.QuestTown;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;

import static joshie.harvest.npc.HFNPCs.GODDESS;

@HFQuest("seeds.strawberry")
public class QuestStrawberry extends QuestTown {
    public QuestStrawberry() {
        setNPCs(GODDESS);
    }

    @Override
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (TownHelper.getClosestTownToEntity(entity).hasBuilding(HFBuildings.GODDESS_POND)) {
            return player.worldObj.rand.nextDouble() <= 0.1D ? getLocalized("thanks") : null;
        }

        return getLocalized("please");
    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, INPC npc, boolean wasSneaking) {
        if (TownHelper.getClosestTownToEntity(entity).hasBuilding(HFBuildings.GODDESS_POND)) {
            complete(player);
        }
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        rewardItem(player, HFCrops.STRAWBERRY.getCropStack(10));
        rewardGold(player, 5000);
    }
}
