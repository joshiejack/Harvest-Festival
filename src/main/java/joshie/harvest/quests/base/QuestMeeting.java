package joshie.harvest.quests.base;

import joshie.harvest.api.buildings.Building;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.quests.Quests;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

public class QuestMeeting extends Quest {
    protected final Building building;
    protected final ItemStack buildingStack;

    public QuestMeeting(Building building, NPC npc) {
        this.building = building;
        this.buildingStack = building.getSpawner();
        setNPCs(npc);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.YULIF_MEET);
    }

    @Override
    public boolean isNPCUsed(EntityPlayer player, NPCEntity entity) {
        return getNPCs().contains(entity.getNPC()) && TownHelper.getClosestTownToEntity(player, false).hasBuilding(building);
    }

    @Override
    public String getDescription(World world, EntityPlayer player) {
        return hasBuilding(player) ? getLocalized("description") : null;
    }

    @Override
    public boolean isRealQuest() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getLocalizedScript(EntityPlayer player, NPCEntity entity) {
        return getLocalized("text");
    }

    protected boolean hasBuilding(EntityPlayer player) {
        return TownHelper.getClosestTownToEntity(player, false).hasBuilding(building);
    }

    @Override
    public void onChatClosed(EntityPlayer player, NPCEntity entity, boolean wasSneaking) {
        complete(player);
    }
}
