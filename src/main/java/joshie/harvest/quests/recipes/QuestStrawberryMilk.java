package joshie.harvest.quests.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.lib.HFQuests;
import joshie.harvest.npc.HFNPCs;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Set;

@HFQuest("recipe.milk.strawberry")
public class QuestStrawberryMilk extends QuestRecipe {
    public QuestStrawberryMilk() {
        super("milk_strawberry", HFNPCs.MILKMAID, 10000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(HFQuests.RECIPE_HOT_MILK);
    }
}
