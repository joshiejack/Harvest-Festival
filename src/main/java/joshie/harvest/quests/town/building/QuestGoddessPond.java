package joshie.harvest.quests.town.building;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.player.RelationshipType;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.base.QuestTown;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;

import static joshie.harvest.npc.HFNPCs.GODDESS;

@HFQuest("building.goddess")
public class QuestGoddessPond extends QuestTown {
    public QuestGoddessPond() {
        setNPCs(GODDESS);
    }

    @Override
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        TownData data = TownHelper.getClosestTownToEntity(player);
        if (data.hasBuilding(HFBuildings.GODDESS_POND)) {
            return getLocalized("thanks");
        }

        return player.worldObj.rand.nextDouble() <= 0.1D && data.getBuildings().size() >= 5 ? getLocalized("please") : null;
    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, INPC npc, boolean wasSneaking) {
        if (TownHelper.getClosestTownToEntity(entity).hasBuilding(HFBuildings.GODDESS_POND)) {
            complete(player);
        }
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        HFApi.player.getRelationsForPlayer(player).affectRelationship(RelationshipType.NPC, HFNPCs.GODDESS.getUUID(), 1000);
        rewardItem(player, HFCrops.STRAWBERRY.getCropStack(10));
        rewardGold(player, 5000);
    }
}
