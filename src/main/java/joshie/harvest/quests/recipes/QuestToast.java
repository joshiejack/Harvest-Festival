package joshie.harvest.quests.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.npc.HFNPCs;

@HFQuest("recipe.toast")
public class QuestToast extends QuestRecipe {
    public QuestToast() {
        super("toast", HFNPCs.FLOWER_GIRL, 5000);
    }
}
