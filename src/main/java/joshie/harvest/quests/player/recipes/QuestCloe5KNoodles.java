package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestRecipe;

@HFQuest("recipe.noodles")
public class QuestCloe5KNoodles extends QuestRecipe {
    public QuestCloe5KNoodles() {
        super("noodles", HFNPCs.DAUGHTER_ADULT, 5000);
    }
}
