package joshie.harvest.quests.recipes;

import joshie.harvest.api.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.lib.HFQuests;
import joshie.harvest.npc.HFNPCs;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Set;

@HFQuest("recipe.omelet.rice")
public class QuestOmeletRice extends QuestRecipe {
    public QuestOmeletRice() {
        super("omelet_rice", HFNPCs.MINER, 10000);
    }

    @Override
    public boolean canStartQuest(EntityPlayer player, Set<Quest> active, Set<Quest> finished) {
        return finished.contains(HFQuests.RECIPE_RICE_SOUP);
    }
}
