package joshie.harvest.quests.recipes;

import joshie.harvest.api.HFQuest;
import joshie.harvest.npc.HFNPCs;

@HFQuest("recipe.porridge")
public class QuestPorridge extends QuestRecipe {
    public QuestPorridge() {
        super("porridge", HFNPCs.CAFE_GRANNY, 5000);
    }
}
