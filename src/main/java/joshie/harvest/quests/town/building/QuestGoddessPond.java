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
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Set;

import static joshie.harvest.npcs.HFNPCs.GODDESS;
import static joshie.harvest.quests.Quests.BUILDING_CARPENTER;

@HFQuest("building.goddess")
public class QuestGoddessPond extends QuestTown {
    private static final ItemStack BUILDING = HFBuildings.GODDESS_POND.getSpawner();

    public QuestGoddessPond() {
        setNPCs(GODDESS);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(BUILDING_CARPENTER);
    }

    @Override
    public String getDescription(World world, EntityPlayer player) {
        if (HFBuildings.GODDESS_POND.getRules().canDo(world, player, 1)) {
            return TownHelper.getClosestTownToEntity(player, false).hasBuilding(HFBuildings.GODDESS_POND) ? getLocalized("description") : getLocalized("build");
        } else return null;
    }

    @Override
    public ItemStack getCurrentIcon(World world, EntityPlayer player) {
        return TownHelper.getClosestTownToEntity(player, false).hasBuilding(HFBuildings.GODDESS_POND) ? primary : BUILDING;
    }

    @Override
    public boolean isNPCUsed(EntityPlayer player, NPC npc) {
        if (npc != HFNPCs.GODDESS) return false;
        TownData data = TownHelper.getClosestTownToEntity(player, false);
        return data.getBuildings().size() >= 5 || data.hasBuilding(HFBuildings.GODDESS_POND);
    }

    @Override
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, NPC npc) {
        return TownHelper.getClosestTownToEntity(player, false).getBuildings().size() >= 5 ? getLocalized("thanks") : getLocalized("please");
    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, NPC npc, boolean wasSneaking) {
        if (TownHelper.getClosestTownToEntity(entity, false).hasBuilding(HFBuildings.GODDESS_POND)) {
            complete(player);
        }
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        HFApi.player.getRelationsForPlayer(player).affectRelationship(HFNPCs.GODDESS, 1000);
        rewardItem(player, HFCrops.STRAWBERRY.getCropStack(10));
        rewardGold(player, 5000);
    }
}
