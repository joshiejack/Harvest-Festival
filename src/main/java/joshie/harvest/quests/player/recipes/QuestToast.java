package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.base.QuestRecipe;

@HFQuest("recipe.toast")
public class QuestToast extends QuestRecipe {
    public QuestToast() {
        super("toast", HFNPCs.FLOWER_GIRL, 5000);
    }
}
