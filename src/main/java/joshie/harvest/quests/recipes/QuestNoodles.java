package joshie.harvest.quests.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.npc.HFNPCs;

@HFQuest("recipe.noodles")
public class QuestNoodles extends QuestRecipe {
    public QuestNoodles() {
        super("noodles", HFNPCs.DAUGHTER_1, 5000);
    }
}
