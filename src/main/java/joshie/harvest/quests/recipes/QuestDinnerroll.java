package joshie.harvest.quests.recipes;

import joshie.harvest.api.HFQuest;
import joshie.harvest.npc.HFNPCs;

@HFQuest("recipe.dinnerroll")
public class QuestDinnerroll extends QuestRecipe {
    public QuestDinnerroll() {
        super("dinnerroll", HFNPCs.CLOCK_WORKER, 5000);
    }
}
