package joshie.harvest.quests.recipes;

import joshie.harvest.api.HFQuest;
import joshie.harvest.npc.HFNPCs;

@HFQuest("recipe.latte.vegetable")
public class QuestVegetableLatte extends QuestRecipe {
    public QuestVegetableLatte() {
        super("latte_vegetable", HFNPCs.PRIEST, 5000);
    }
}
