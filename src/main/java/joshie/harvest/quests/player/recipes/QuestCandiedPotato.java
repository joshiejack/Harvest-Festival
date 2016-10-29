package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.base.QuestRecipe;

@HFQuest("recipe.potato.candied")
public class QuestCandiedPotato extends QuestRecipe {
    public QuestCandiedPotato() {
        super("potato_candied", HFNPCs.TRADER, 5000);
    }
}
