package joshie.harvest.quests.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.quests.Quests;
import joshie.harvest.npc.HFNPCs;

import java.util.Set;

@HFQuest("recipe.cake.chocolate")
public class QuestChocolateCake extends QuestRecipe {
    public QuestChocolateCake() {
        super("cake_chocolate", HFNPCs.BUILDER, 10000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.RECIPE_PINEAPPLE_JUICE);
    }
}
