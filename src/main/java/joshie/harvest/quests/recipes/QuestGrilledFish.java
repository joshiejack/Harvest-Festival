package joshie.harvest.quests.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.quests.Quests;
import joshie.harvest.npc.HFNPCs;

import java.util.Set;

@HFQuest("recipe.fish.grilled")
public class QuestGrilledFish extends QuestRecipe {
    public QuestGrilledFish() {
        super("fish_grilled", HFNPCs.FISHERMAN, 10000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.RECIPE_FISH_STEW);
    }
}
