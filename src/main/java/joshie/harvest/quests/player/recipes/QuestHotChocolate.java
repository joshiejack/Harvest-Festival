package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.base.QuestRecipe;

@HFQuest("recipe.chocolate.hot")
public class QuestHotChocolate extends QuestRecipe {
    public QuestHotChocolate() {
        super("chocolate_hot", HFNPCs.CAFE_OWNER, 5000);
    }
}
