package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.quests.Quests;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

@HFQuest("recipe.risotto")
public class QuestCloe10KRisotto extends QuestRecipe {
    public QuestCloe10KRisotto() {
        super("risotto", HFNPCs.DAUGHTER_ADULT, 10000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.CLOE_5K);
    }
}
