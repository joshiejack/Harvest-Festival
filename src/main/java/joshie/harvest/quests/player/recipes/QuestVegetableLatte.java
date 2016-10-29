package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.base.QuestRecipe;

@HFQuest("recipe.latte.vegetable")
public class QuestVegetableLatte extends QuestRecipe {
    public QuestVegetableLatte() {
        super("latte_vegetable", HFNPCs.PRIEST, 5000);
    }
}
