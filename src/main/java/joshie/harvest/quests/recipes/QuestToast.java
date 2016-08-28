package joshie.harvest.quests.recipes;

import joshie.harvest.api.HFQuest;
import joshie.harvest.npc.HFNPCs;

@HFQuest("recipe.toast")
public class QuestToast extends QuestRecipe {
    public QuestToast() {
        super("toast", HFNPCs.SEED_OWNER, 5000);
    }
}
