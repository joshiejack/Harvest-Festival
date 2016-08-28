package joshie.harvest.quests.recipes;

import joshie.harvest.api.HFQuest;
import joshie.harvest.npc.HFNPCs;

@HFQuest("recipe.juice.pineapple")
public class QuestPineappleJuice extends QuestRecipe {
    public QuestPineappleJuice() {
        super("juice_pineapple", HFNPCs.BUILDER, 5000);
    }
}
