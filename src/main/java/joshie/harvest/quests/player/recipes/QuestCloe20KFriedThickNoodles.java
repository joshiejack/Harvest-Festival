package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

@HFQuest("recipe.noodles.thick.fried")
public class QuestCloe20KFriedThickNoodles extends QuestRecipe {
    public QuestCloe20KFriedThickNoodles() {
        super("noodles_thick_fried", HFNPCs.DAUGHTER_ADULT, 20000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.CLOE_15K);
    }
}
