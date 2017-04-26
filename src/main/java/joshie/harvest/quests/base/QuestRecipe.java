package joshie.harvest.quests.base;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.cooking.CookingHelper;
import joshie.harvest.quests.Quests;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.Set;

public class QuestRecipe extends QuestFriendship {
    public final String[] recipe;

    public QuestRecipe(String recipe, NPC npc, int relationship) {
        super(npc, relationship);
        this.recipe = new String[]{recipe};
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
    protected NonNullList<ItemStack> getRewardStacks(EntityPlayer player) {
        NonNullList<ItemStack> stacks = NonNullList.withSize(recipe.length, ItemStack.EMPTY);
        for (int i = 0; i < stacks.size(); i++) {
            stacks.set(i, CookingHelper.getRecipe(recipe[i]));
        }

        return stacks;
    }
}