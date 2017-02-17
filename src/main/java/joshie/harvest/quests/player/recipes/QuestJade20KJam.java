package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

@HFQuest("recipe.jam")
public class QuestJade20KJam extends QuestRecipe {
    public QuestJade20KJam() {
        super(HFNPCs.FLOWER_GIRL, 20000, "jam_apple", "jam_grape", "marmalade");
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.JADE_15K);
    }
}
