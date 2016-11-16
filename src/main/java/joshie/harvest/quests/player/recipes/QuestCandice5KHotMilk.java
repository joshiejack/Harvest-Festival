package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

@HFQuest("recipe.milk.hot")
public class QuestCandice5KHotMilk extends QuestRecipe {
    public QuestCandice5KHotMilk() {
        super("milk_hot", HFNPCs.MILKMAID, 5000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.CANDICE_MEET);
    }
}
