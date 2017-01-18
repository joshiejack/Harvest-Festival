package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

@HFQuest("recipe.potato.candied")
public class QuestJohan5KCandiedPotato extends QuestRecipe {
    public QuestJohan5KCandiedPotato() {
        super("potato_candied", HFNPCs.TRADER, 5000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.JOHAN_MEET);
    }
}
