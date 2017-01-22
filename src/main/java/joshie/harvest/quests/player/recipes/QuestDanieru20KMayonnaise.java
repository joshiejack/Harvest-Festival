package joshie.harvest.quests.player.recipes;

import com.google.common.collect.Lists;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.cooking.CookingHelper;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Set;

@HFQuest("recipe.mayonnaise")
public class QuestDanieru20KMayonnaise extends QuestRecipe {
    public QuestDanieru20KMayonnaise() {
        super("mayonnaise", HFNPCs.BLACKSMITH, 20000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.DANIERU_15K);
    }

    @Override
    protected List<ItemStack> getRewardStacks(EntityPlayer player) {
        return Lists.newArrayList(CookingHelper.getRecipe("mayonnaise_small"),
                                  CookingHelper.getRecipe("mayonnaise_medium"),
                                  CookingHelper.getRecipe("mayonnaise_large"));
    }
}
