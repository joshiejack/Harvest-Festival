package joshie.harvest.quests.player.recipes;

import com.google.common.collect.Lists;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.cooking.CookingHelper;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Set;

@HFQuest("recipe.jam")
public class QuestJade20KJam extends QuestRecipe {
    private final String recipe2;
    private final String recipe3;
    public QuestJade20KJam() {
        super("jam_apple", HFNPCs.FLOWER_GIRL, 20000);
        recipe2 = "jam_grape";
        recipe3 = "marmalade";
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.JADE_15K);
    }

    @Override
    protected List<ItemStack> getRewardStacks(EntityPlayer player) {
        return Lists.newArrayList(CookingHelper.getRecipe(recipe), CookingHelper.getRecipe(recipe2), CookingHelper.getRecipe(recipe3));
    }
}
