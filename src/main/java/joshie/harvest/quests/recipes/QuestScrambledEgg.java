package joshie.harvest.quests.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.lib.HFQuests;
import joshie.harvest.npc.HFNPCs;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Set;

@HFQuest("recipe.egg.scrambled")
public class QuestScrambledEgg extends QuestRecipe {
    public QuestScrambledEgg() {
        super("egg_scrambled", HFNPCs.TOOL_OWNER, 10000);
    }

    @Override
    public boolean canStartQuest(EntityPlayer player, Set<Quest> active, Set<Quest> finished) {
        return finished.contains(HFQuests.RECIPE_BUTTER);
    }
}
