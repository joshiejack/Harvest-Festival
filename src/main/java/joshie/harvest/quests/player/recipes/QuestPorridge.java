package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.base.QuestRecipe;

@HFQuest("recipe.porridge")
public class QuestPorridge extends QuestRecipe {
    public QuestPorridge() {
        super("porridge", HFNPCs.CAFE_GRANNY, 5000);
    }
}
