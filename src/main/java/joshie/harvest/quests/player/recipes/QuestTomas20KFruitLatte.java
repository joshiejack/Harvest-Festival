package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

@HFQuest("recipe.latte.fruit")
public class QuestTomas20KFruitLatte extends QuestRecipe {
    public QuestTomas20KFruitLatte() {
        super("latte_fruit", HFNPCs.PRIEST, 20000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.TOMAS_15K);
    }
}
