package joshie.harvest.quests.recipes;

import joshie.harvest.api.HFQuest;
import joshie.harvest.npc.HFNPCs;

@HFQuest("recipe.chocolate.hot")
public class QuestHotChocolate extends QuestRecipe {
    public QuestHotChocolate() {
        super("chocolate_hot", HFNPCs.CAFE_OWNER, 5000);
    }
}
