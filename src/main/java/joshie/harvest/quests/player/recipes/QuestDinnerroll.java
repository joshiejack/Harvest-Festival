package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.base.QuestRecipe;

@HFQuest("recipe.dinnerroll")
public class QuestDinnerroll extends QuestRecipe {
    public QuestDinnerroll() {
        super("dinnerroll", HFNPCs.CLOCK_WORKER, 5000);
    }
}
