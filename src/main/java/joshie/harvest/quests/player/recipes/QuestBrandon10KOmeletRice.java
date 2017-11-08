package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

@HFQuest("recipe.omelet.rice")
public class QuestBrandon10KOmeletRice extends QuestRecipe {
    public QuestBrandon10KOmeletRice() {
        super("omelet_rice", HFNPCs.MINER, 10000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.BRANDON_5K);
    }
}
