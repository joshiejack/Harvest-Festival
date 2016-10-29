package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.base.QuestRecipe;

@HFQuest("recipe.fish.stew")
public class QuestFishStew extends QuestRecipe {
    public QuestFishStew() {
        super("stew_fish", HFNPCs.FISHERMAN, 5000);
    }
}
