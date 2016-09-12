package joshie.harvest.quests.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.quests.Quests;
import joshie.harvest.npc.HFNPCs;

import java.util.Set;

@HFQuest("recipe.omelet.rice")
public class QuestOmeletRice extends QuestRecipe {
    public QuestOmeletRice() {
        super("omelet_rice", HFNPCs.MINER, 10000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.RECIPE_RICE_SOUP);
    }
}
