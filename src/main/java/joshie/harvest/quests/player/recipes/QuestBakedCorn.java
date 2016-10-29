package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.base.QuestRecipe;

@HFQuest("recipe.corn.baked")
public class QuestBakedCorn extends QuestRecipe {
    public QuestBakedCorn() {
        super("corn_baked", HFNPCs.POULTRY, 5000);
    }
}
