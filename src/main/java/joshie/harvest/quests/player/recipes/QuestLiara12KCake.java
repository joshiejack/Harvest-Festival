package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

@HFQuest("recipe.cookies")
public class QuestLiara12KCake extends QuestRecipe {
    public QuestLiara12KCake() {
        super("cake", HFNPCs.CAFE_OWNER, 12500);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.LIARA_10K);
    }
}
