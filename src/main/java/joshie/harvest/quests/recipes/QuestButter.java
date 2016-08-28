package joshie.harvest.quests.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.npc.HFNPCs;

@HFQuest("recipe.butter")
public class QuestButter extends QuestRecipe {
    public QuestButter() {
        super("butter", HFNPCs.TOOL_OWNER, 5000);
    }
}
