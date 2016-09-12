package joshie.harvest.quests.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.quests.Quests;
import joshie.harvest.npc.HFNPCs;

import java.util.Set;

@HFQuest("recipe.cookies.chocolate")
public class QuestChocolateCookies extends QuestRecipe {
    public QuestChocolateCookies() {
        super("cookies_chocolate", HFNPCs.CAFE_OWNER, 10000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.RECIPE_COOKIES);
    }
}
