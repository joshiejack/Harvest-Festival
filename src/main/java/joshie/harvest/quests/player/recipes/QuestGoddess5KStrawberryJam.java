package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

@HFQuest("recipe.jam.strawberry")
public class QuestGoddess5KStrawberryJam extends QuestRecipe {
    public QuestGoddess5KStrawberryJam() {
        super("jam_strawberry", HFNPCs.GODDESS, 5000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.GODDESS_MEET);
    }
}
