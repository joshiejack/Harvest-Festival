package joshie.harvest.quests.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.lib.HFQuests;
import joshie.harvest.npc.HFNPCs;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Set;

@HFQuest("recipe.doughnut")
public class QuestDoughnut extends QuestRecipe {
    public QuestDoughnut() {
        super("doughnut", HFNPCs.CLOCK_WORKER, 10000);
    }

    @Override
    public boolean canStartQuest(EntityPlayer player, Set<Quest> active, Set<Quest> finished) {
        return finished.contains(HFQuests.RECIPE_DINNERROLL);
    }
}
