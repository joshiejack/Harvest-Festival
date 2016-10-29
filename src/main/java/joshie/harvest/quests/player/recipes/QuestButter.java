package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.base.QuestRecipe;

@HFQuest("recipe.butter")
public class QuestButter extends QuestRecipe {
    public QuestButter() {
        super("butter", HFNPCs.BLACKSMITH, 5000);
    }
}
