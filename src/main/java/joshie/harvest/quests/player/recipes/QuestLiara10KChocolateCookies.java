package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

@HFQuest("recipe.cookies.chocolate")
public class QuestLiara10KChocolateCookies extends QuestRecipe {
    public QuestLiara10KChocolateCookies() {
        super("cookies_chocolate", HFNPCs.CAFE_OWNER, 10000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.LIARA_7K);
    }
}
