package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.quests.Quests;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

@HFQuest("recipe.cookies")
public class QuestLiara7KCookies extends QuestRecipe {
    public QuestLiara7KCookies() {
        super("cookies", HFNPCs.CAFE_OWNER, 7500);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.LIARA_5K);
    }
}
