package joshie.harvest.quests.recipes;

import joshie.harvest.api.HFQuest;
import joshie.harvest.npc.HFNPCs;

@HFQuest("recipe.potato.candied")
public class QuestCandiedPotato extends QuestRecipe {
    public QuestCandiedPotato() {
        super("potato_candied", HFNPCs.TRADER, 5000);
    }
}
