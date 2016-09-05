package joshie.harvest.quests.recipes;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.lib.HFQuests;
import joshie.harvest.npc.HFNPCs;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Set;

@HFQuest("recipe.pancake.savoury")
public class QuestPancakeSavoury extends QuestRecipe {
    public QuestPancakeSavoury() {
        super("pancake_savoury", HFNPCs.ANIMAL_OWNER, 10000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(HFQuests.RECIPE_FISH_FINGERS);
    }
}
