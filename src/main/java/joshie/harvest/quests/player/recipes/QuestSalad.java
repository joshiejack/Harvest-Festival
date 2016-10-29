package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.base.QuestRecipe;

@HFQuest("recipe.salad")
public class QuestSalad extends QuestRecipe {
    public QuestSalad() {
        super("salad", HFNPCs.GS_OWNER, 5000);
    }
}
