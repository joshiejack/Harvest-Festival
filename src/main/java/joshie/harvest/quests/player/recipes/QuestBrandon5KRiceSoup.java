package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestRecipe;

import java.util.Set;

@HFQuest("recipe.soup.rice")
public class QuestBrandon5KRiceSoup extends QuestRecipe {
    public QuestBrandon5KRiceSoup() {
        super("soup_rice", HFNPCs.MINER, 5000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.BRANDON_MEET);
    }
}
