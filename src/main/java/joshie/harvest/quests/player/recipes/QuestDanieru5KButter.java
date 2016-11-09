package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.base.QuestRecipe;

@HFQuest("recipe.butter")
public class QuestDanieru5KButter extends QuestRecipe {
    public QuestDanieru5KButter() {
        super("butter", HFNPCs.BLACKSMITH, 5000);
    }
}
