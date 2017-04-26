package joshie.harvest.quests.town.building;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.town.Town;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestTown;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Set;

import static joshie.harvest.npcs.HFNPCs.GODDESS;

@HFQuest("building.goddess")
public class QuestGoddessPond extends QuestTown {
    private static final ItemStack BUILDING = HFBuildings.GODDESS_POND.getSpawner();

    public QuestGoddessPond() {
        setNPCs(GODDESS);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return true;
    }

    @Override
    public boolean isNPCUsed(EntityPlayer player, NPCEntity entity) {
        Town data = entity.getTown();
        return super.isNPCUsed(player, entity) && (data.getBuildingCount() >= 5 || data.hasBuilding(HFBuildings.GODDESS_POND));
    }

    @Override
    public String getDescription(World world, EntityPlayer player) {
        if (HFBuildings.GODDESS_POND.getRules().canDo(world, player, 1)) {
            return HFApi.towns.getTownForEntity(player).hasBuilding(HFBuildings.GODDESS_POND) ? getLocalized("description") : getLocalized("build");
        } else return null;
    }

    @Override
    @Nonnull
    public ItemStack getCurrentIcon(World world, EntityPlayer player) {
        return HFApi.towns.getTownForEntity(player).hasBuilding(HFBuildings.GODDESS_POND) ? primary : BUILDING;
    }

    @Override
    public String getLocalizedScript(EntityPlayer player, NPCEntity entity) {
        return entity.getTown().hasBuilding(HFBuildings.GODDESS_POND) ? getLocalized("thanks") : getLocalized("please");
    }

    @Override
    public void onChatClosed(EntityPlayer player, NPCEntity entity, boolean wasSneaking) {
        if (entity.getTown().hasBuilding(HFBuildings.GODDESS_POND)) {
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
