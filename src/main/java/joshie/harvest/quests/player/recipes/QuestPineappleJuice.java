package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.base.QuestRecipe;

@HFQuest("recipe.juice.pineapple")
public class QuestPineappleJuice extends QuestRecipe {
    public QuestPineappleJuice() {
        super("juice_pineapple", HFNPCs.BUILDER, 5000);
    }
}
