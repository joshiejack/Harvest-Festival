package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.quests.Quests;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

@HFQuest("recipe.doughnut")
public class QuestTiberius10KDoughnut extends QuestRecipe {
    public QuestTiberius10KDoughnut() {
        super("doughnut", HFNPCs.CLOCKMAKER, 10000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.TIBERIUS_5K);
    }
}
