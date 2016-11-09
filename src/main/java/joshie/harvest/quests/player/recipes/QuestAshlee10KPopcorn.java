package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.quests.Quests;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

@HFQuest("recipe.popcorn")
public class QuestAshlee10KPopcorn extends QuestRecipe {
    public QuestAshlee10KPopcorn() {
        super("popcorn", HFNPCs.POULTRY, 10000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.ASHLEE_5K);
    }
}
