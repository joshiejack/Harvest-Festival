package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

@HFQuest("recipe.fishsticks")
public class QuestJim5KFishsticks extends QuestRecipe {
    public QuestJim5KFishsticks() {
        super("fishsticks", HFNPCs.BARN_OWNER, 5000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.JIM_MEET);
    }
}
