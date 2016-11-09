package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

@HFQuest("recipe.corn.baked")
public class QuestAshlee5KBakedCorn extends QuestRecipe {
    public QuestAshlee5KBakedCorn() {
        super("corn_baked", HFNPCs.POULTRY, 5000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.ASHLEE_MEET);
    }
}
