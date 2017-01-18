package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.quests.Quests;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

@HFQuest("recipe.fish.grilled")
public class QuestJacob10KGrilledFish extends QuestRecipe {
    public QuestJacob10KGrilledFish() {
        super("fish_grilled", HFNPCs.FISHERMAN, 10000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.JACOB_5K);
    }
}
