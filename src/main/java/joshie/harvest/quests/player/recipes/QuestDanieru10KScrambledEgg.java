package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.quests.Quests;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

@HFQuest("recipe.egg.scrambled")
public class QuestDanieru10KScrambledEgg extends QuestRecipe {
    public QuestDanieru10KScrambledEgg() {
        super("egg_scrambled", HFNPCs.BLACKSMITH, 10000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.DANIERU_5K);
    }
}
