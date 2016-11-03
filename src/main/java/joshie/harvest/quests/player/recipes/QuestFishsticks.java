package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.base.QuestRecipe;

@HFQuest("recipe.fishsticks")
public class QuestFishsticks extends QuestRecipe {
    public QuestFishsticks() {
        super("fishsticks", HFNPCs.BARN_OWNER, 5000);
    }
}
