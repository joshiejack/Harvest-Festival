package joshie.harvest.quests.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.quests.Quests;
import joshie.harvest.npc.HFNPCs;

import java.util.Set;

@HFQuest("recipe.cookies")
public class QuestCookies extends QuestRecipe {
    public QuestCookies() {
        super("cookies", HFNPCs.CAFE_OWNER, 7500);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.RECIPE_HOT_CHOCOLATE);
    }
}
