package joshie.harvest.quests.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.quests.Quests;
import joshie.harvest.npc.HFNPCs;

import java.util.Set;

@HFQuest("recipe.egg.scrambled")
public class QuestScrambledEgg extends QuestRecipe {
    public QuestScrambledEgg() {
        super("egg_scrambled", HFNPCs.BLACKSMITH, 10000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.RECIPE_BUTTER);
    }
}
