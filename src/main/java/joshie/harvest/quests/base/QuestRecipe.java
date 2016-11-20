package joshie.harvest.quests.base;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.cooking.CookingHelper;
import joshie.harvest.quests.Quests;
import net.minecraft.item.ItemStack;

import java.util.Set;

public class QuestRecipe extends QuestFriendship {
    protected final String recipe;

    public QuestRecipe(String recipe, INPC npc, int relationship) {
        super(npc, relationship);
        this.recipe = recipe;
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.LIARA_MEET);
    }

    @Override
    protected ItemStack getRewardStack() {
        return CookingHelper.getRecipe(recipe);
    }
}
