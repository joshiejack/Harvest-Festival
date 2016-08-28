package joshie.harvest.quests.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.npc.HFNPCs;

@HFQuest("recipe.fishsticks")
public class QuestFishsticks extends QuestRecipe {
    public QuestFishsticks() {
        super("fishsticks", HFNPCs.ANIMAL_OWNER, 5000);
    }
}
