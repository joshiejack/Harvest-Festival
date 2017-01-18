package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

@HFQuest("recipe.rice.bamboo")
public class QuestBrandon20KBambooRice extends QuestRecipe {
    public QuestBrandon20KBambooRice() {
        super("rice_bamboo", HFNPCs.MINER, 20000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.BRANDON_15K);
    }
}
