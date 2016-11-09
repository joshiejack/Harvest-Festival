package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.base.QuestRecipe;

@HFQuest("recipe.dinnerroll")
public class QuestTiberius5KDinnerroll extends QuestRecipe {
    public QuestTiberius5KDinnerroll() {
        super("dinnerroll", HFNPCs.CLOCKMAKER, 5000);
    }
}
