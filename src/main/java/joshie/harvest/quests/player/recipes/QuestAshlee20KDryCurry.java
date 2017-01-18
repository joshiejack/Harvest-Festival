package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

@HFQuest("recipe.curry.dry")
public class QuestAshlee20KDryCurry extends QuestRecipe {
    public QuestAshlee20KDryCurry() {
        super("curry_dry", HFNPCs.POULTRY, 20000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.ASHLEE_15K);
    }
}
