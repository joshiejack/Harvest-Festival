package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

@HFQuest("recipe.tempura.rice")
public class QuestLiara20KTempuraRice extends QuestRecipe {
    public QuestLiara20KTempuraRice() {
        super("tempura_rice", HFNPCs.CAFE_OWNER, 20000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.LIARA_17K);
    }
}
