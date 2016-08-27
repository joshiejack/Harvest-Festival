package joshie.harvest.quests.recipes;

import joshie.harvest.api.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.lib.HFQuests;
import joshie.harvest.npc.HFNPCs;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Set;

@HFQuest(data = "recipe.cake.chocolate")
public class QuestChocolateCake extends QuestRecipe {
    public QuestChocolateCake() {
        super("cake_chocolate", HFNPCs.BUILDER, 10000);
    }

    @Override
    public boolean canStartQuest(EntityPlayer player, Set<Quest> active, Set<Quest> finished) {
        return finished.contains(HFQuests.RECIPE_PINEAPPLE_JUICE);
    }
}
