package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

@HFQuest("recipe.tempura.noodles")
public class QuestLiara22KTempuraNoodles extends QuestRecipe {
    public QuestLiara22KTempuraNoodles() {
        super("tempura_noodles", HFNPCs.CAFE_OWNER, 22500);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.LIARA_20K);
    }
}
