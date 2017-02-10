package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

import static joshie.harvest.quests.Quests.TOMAS_MEET;

@HFQuest("recipe.latte.vegetable")
public class QuestTomas5KVegetableLatte extends QuestRecipe {
    public QuestTomas5KVegetableLatte() {
        super("latte_vegetable", HFNPCs.PRIEST, 5000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(TOMAS_MEET);
    }
}
