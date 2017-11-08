package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

import static joshie.harvest.quests.Quests.CLOE_MEET;

@HFQuest("recipe.noodles")
public class QuestCloe5KNoodles extends QuestRecipe {
    public QuestCloe5KNoodles() {
        super("noodles", HFNPCs.DAUGHTER_ADULT, 5000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(CLOE_MEET);
    }
}
