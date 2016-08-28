package joshie.harvest.quests.recipes;

import joshie.harvest.api.HFQuest;
import joshie.harvest.npc.HFNPCs;

@HFQuest("recipe.corn.baked")
public class QuestBakedCorn extends QuestRecipe {
    public QuestBakedCorn() {
        super("corn_baked", HFNPCs.POULTRY, 5000);
    }
}
