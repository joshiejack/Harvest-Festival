package joshie.harvest.quests.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.lib.HFQuests;
import joshie.harvest.npc.HFNPCs;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Set;

@HFQuest("recipe.fish.grilled")
public class QuestGrilledFish extends QuestRecipe {
    public QuestGrilledFish() {
        super("fish_grilled", HFNPCs.FISHERMAN, 10000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(HFQuests.RECIPE_FISH_STEW);
    }
}
