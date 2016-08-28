package joshie.harvest.quests.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.npc.HFNPCs;

@HFQuest("recipe.salad")
public class QuestSalad extends QuestRecipe {
    public QuestSalad() {
        super("salad", HFNPCs.GS_OWNER, 5000);
    }
}
