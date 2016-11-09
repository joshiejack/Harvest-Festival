package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

@HFQuest("recipe.fish.stew")
public class QuestJacob5KFishStew extends QuestRecipe {
    public QuestJacob5KFishStew() {
        super("stew_fish", HFNPCs.FISHERMAN, 5000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.JACOB_MEET);
    }
}
