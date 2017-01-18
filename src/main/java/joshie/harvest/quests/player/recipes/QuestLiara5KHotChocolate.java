package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestRecipe;

@HFQuest("recipe.chocolate.hot")
public class QuestLiara5KHotChocolate extends QuestRecipe {
    public QuestLiara5KHotChocolate() {
        super("chocolate_hot", HFNPCs.CAFE_OWNER, 5000);
    }
}
