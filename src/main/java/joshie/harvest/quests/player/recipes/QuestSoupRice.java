package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.base.QuestRecipe;

@HFQuest("recipe.soup.rice")
public class QuestSoupRice extends QuestRecipe {
    public QuestSoupRice() {
        super("soup_rice", HFNPCs.MINER, 5000);
    }
}
