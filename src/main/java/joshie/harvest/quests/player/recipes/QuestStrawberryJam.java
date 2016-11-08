package joshie.harvest.quests.player.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.base.QuestRecipe;

@HFQuest("recipe.jam.strawberry")
public class QuestStrawberryJam extends QuestRecipe {
    public QuestStrawberryJam() {
        super("jam_strawberry", HFNPCs.GODDESS, 5000);
    }
}
