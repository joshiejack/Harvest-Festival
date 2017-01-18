package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

@HFQuest("recipe.porridge")
public class QuestKatlin5KPorridge extends QuestRecipe {
    public QuestKatlin5KPorridge() {
        super("porridge", HFNPCs.CAFE_GRANNY, 5000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.KATLIN_MEET);
    }
}
