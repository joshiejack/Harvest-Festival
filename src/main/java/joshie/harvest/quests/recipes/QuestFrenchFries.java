package joshie.harvest.quests.recipes;

import joshie.harvest.api.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.lib.HFQuests;
import joshie.harvest.npc.HFNPCs;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Set;

@HFQuest("recipe.fries.french")
public class QuestFrenchFries extends QuestRecipe {
    public QuestFrenchFries() {
        super("fries_french", HFNPCs.TRADER, 10000);
    }

    @Override
    public boolean canStartQuest(EntityPlayer player, Set<Quest> active, Set<Quest> finished) {
        return finished.contains(HFQuests.RECIPE_CANDIED_POTATO);
    }
}
