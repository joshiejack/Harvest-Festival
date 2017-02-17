package joshie.harvest.quests.base;

import com.google.common.collect.Lists;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.cooking.CookingHelper;
import joshie.harvest.quests.Quests;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Set;

public class QuestRecipe extends QuestFriendship {
    public final String[] recipe;

    public QuestRecipe(String recipe, NPC npc, int relationship) {
        super(npc, relationship);
        this.recipe = new String[] { recipe };
    }

    public QuestRecipe(NPC npc, int relationship, String... recipes) {
        super(npc, relationship);
        this.recipe = recipes;
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.LIARA_MEET);
    }

    @Override
    protected List<ItemStack> getRewardStacks(EntityPlayer player) {
        ItemStack[] stacks = new ItemStack[recipe.length];
        for (int i = 0; i < stacks.length; i++) {
            stacks[i] = CookingHelper.getRecipe(recipe[i]);
        }

        return Lists.newArrayList(stacks);
    }
}
