package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

@HFQuest("recipe.mayonnaise")
public class QuestDanieru20KMayonnaise extends QuestRecipe {
    public QuestDanieru20KMayonnaise() {
        super(HFNPCs.BLACKSMITH, 20000, "mayonnaise_small", "mayonnaise_medium", "mayonnaise_large");
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.DANIERU_15K);
    }
}
