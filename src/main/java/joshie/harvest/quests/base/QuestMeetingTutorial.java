package joshie.harvest.quests.base;

import joshie.harvest.api.buildings.Building;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.QuestQuestion;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.Set;

import static joshie.harvest.quests.Quests.JADE_MEET;

public abstract class QuestMeetingTutorial extends QuestQuestion {
    protected final Building building;
    protected final ItemStack buildingStack;

    public QuestMeetingTutorial(QuestSelection selection, Building building, NPC... npc) {
        super(selection);
        this.setNPCs(npc);
        this.building = building;
        this.buildingStack = building.getSpawner();
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(JADE_MEET);
    }

    @Override
    public boolean isNPCUsed(EntityPlayer player, NPC npc) {
        return getNPCs().contains(npc) && hasBuilding(player);
    }

    protected boolean hasBuilding(EntityPlayer player) {
        return TownHelper.getClosestTownToEntity(player).hasBuilding(building);
    }
}
