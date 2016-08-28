package joshie.harvest.quests.recipes;

import joshie.harvest.api.HFQuest;
import joshie.harvest.npc.HFNPCs;

@HFQuest("recipe.fish.stew")
public class QuestFishStew extends QuestRecipe {
    public QuestFishStew() {
        super("stew_fish", HFNPCs.FISHERMAN, 5000);
    }
}
