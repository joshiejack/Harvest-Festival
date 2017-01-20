package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

@HFQuest("recipe.souffle.apple")
public class QuestYulif20KAppleSouffle extends QuestRecipe {
    public QuestYulif20KAppleSouffle() {
        super("souffle_apple", HFNPCs.CARPENTER, 20000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.YULIF_15K);
    }
}
