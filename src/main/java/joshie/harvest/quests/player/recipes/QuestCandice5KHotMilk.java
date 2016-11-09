package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.base.QuestRecipe;

@HFQuest("recipe.milk.hot")
public class QuestCandice5KHotMilk extends QuestRecipe {
    public QuestCandice5KHotMilk() {
        super("milk_hot", HFNPCs.MILKMAID, 5000);
    }
}
