package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.quests.Quests;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

@HFQuest("recipe.milk.strawberry")
public class QuestGoddess10KStrawberryMilk extends QuestRecipe {
    public QuestGoddess10KStrawberryMilk() {
        super("milk_strawberry", HFNPCs.GODDESS, 10000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.GODDESS_5K);
    }
}
