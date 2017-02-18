package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

@HFQuest("recipe.bread.raisin")
public class QuestTiberius20KRaisinBread extends QuestRecipe {
    public QuestTiberius20KRaisinBread() {
        super("bread_raisin", HFNPCs.CLOCKMAKER, 20000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.TIBERIUS_15K);
    }
}
