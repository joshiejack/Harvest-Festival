package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

@HFQuest("recipe.jam.apple")
public class QuestJade25KAppleJam extends QuestRecipe {
    public QuestJade25KAppleJam() {
        super("bun_jam", HFNPCs.FLOWER_GIRL, 25000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.JADE_20K);
    }
}
