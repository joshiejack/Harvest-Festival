package joshie.harvest.quests.recipes;

import joshie.harvest.api.HFQuest;
import joshie.harvest.npc.HFNPCs;

@HFQuest(data = "recipe.toast")
public class QuestToast extends QuestRecipe {
    public QuestToast() {
        super("toast", HFNPCs.SEED_OWNER, 5000);
    }
}
