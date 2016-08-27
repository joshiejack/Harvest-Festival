package joshie.harvest.quests.recipes;

import joshie.harvest.api.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.lib.HFQuests;
import joshie.harvest.npc.HFNPCs;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Set;

@HFQuest(data = "recipe.milk.hot")
public class QuestHotMilk extends QuestRecipe {
    public QuestHotMilk() {
        super("milk_hot", HFNPCs.MILKMAID, 5000);
    }

    @Override
    public boolean canStartQuest(EntityPlayer player, Set<Quest> active, Set<Quest> finished) {
        return finished.contains(HFQuests.TUTORIAL_SUPERMARKET);
    }
}
