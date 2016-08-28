package joshie.harvest.quests.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.npc.HFNPCs;

@HFQuest("recipe.milk.hot")
public class QuestHotMilk extends QuestRecipe {
    public QuestHotMilk() {
        super("milk_hot", HFNPCs.MILKMAID, 5000);
    }
}
