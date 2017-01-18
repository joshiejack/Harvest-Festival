package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

@HFQuest("recipe.jam.apple")
public class QuestJade25KJamBun extends QuestRecipe {
    public QuestJade25KJamBun() {
        super("bun_jam", HFNPCs.FLOWER_GIRL, 25000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.JADE_20K);
    }
}
