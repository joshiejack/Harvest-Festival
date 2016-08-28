package joshie.harvest.quests.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.npc.HFNPCs;

@HFQuest("recipe.soup.rice")
public class QuestSoupRice extends QuestRecipe {
    public QuestSoupRice() {
        super("soup_rice", HFNPCs.MINER, 5000);
    }
}
