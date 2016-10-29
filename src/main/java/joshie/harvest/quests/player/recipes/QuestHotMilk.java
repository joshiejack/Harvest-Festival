package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.base.QuestRecipe;

@HFQuest("recipe.milk.hot")
public class QuestHotMilk extends QuestRecipe {
    public QuestHotMilk() {
        super("milk_hot", HFNPCs.MILKMAID, 5000);
    }
}
