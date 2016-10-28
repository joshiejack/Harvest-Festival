package joshie.harvest.quests.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.quests.Quests;
import joshie.harvest.npc.HFNPCs;

import java.util.Set;

@HFQuest("recipe.toast.french")
public class QuestFrenchToast extends QuestRecipe {
    public QuestFrenchToast() {
        super("toast_french", HFNPCs.FLOWER_GIRL, 10000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.RECIPE_TOAST);
    }
}
