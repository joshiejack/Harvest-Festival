package joshie.harvest.quests.town.building;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestTown;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Set;

import static joshie.harvest.npcs.HFNPCs.GODDESS;
import static joshie.harvest.quests.Quests.BUILDING_CARPENTER;

@HFQuest("building.goddess")
public class QuestGoddessPond extends QuestTown {
    public QuestGoddessPond() {
        setNPCs(GODDESS);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(BUILDING_CARPENTER);
    }

    @Override
    public boolean isNPCUsed(EntityPlayer player, NPC npc) {
        if (npc != HFNPCs.GODDESS) return false;
        TownData data = TownHelper.getClosestTownToEntity(player);
        return data.getBuildings().size() >= 5 || data.hasBuilding(HFBuildings.GODDESS_POND);
    }

    @Override
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, NPC npc) {
        return TownHelper.getClosestTownToEntity(player).getBuildings().size() >= 5 ? getLocalized("please") : getLocalized("thanks");
    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, NPC npc, boolean wasSneaking) {
        if (TownHelper.getClosestTownToEntity(entity).hasBuilding(HFBuildings.GODDESS_POND)) {
            complete(player);
        }
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        HFApi.player.getRelationsForPlayer(player).affectRelationship(HFNPCs.GODDESS.getUUID(), 1000);
        rewardItem(player, HFCrops.STRAWBERRY.getCropStack(10));
        rewardGold(player, 5000);
    }
}
