package joshie.harvest.quests.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.quests.Quests;
import joshie.harvest.npc.HFNPCs;

import java.util.Set;

@HFQuest("recipe.pancake.savoury")
public class QuestPancakeSavoury extends QuestRecipe {
    public QuestPancakeSavoury() {
        super("pancake_savoury", HFNPCs.ANIMAL_OWNER, 10000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.RECIPE_FISH_FINGERS);
    }
}
