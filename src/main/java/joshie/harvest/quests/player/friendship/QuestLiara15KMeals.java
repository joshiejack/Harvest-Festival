package joshie.harvest.quests.player.friendship;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.cooking.CookingHelper;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestFriendshipStore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static joshie.harvest.core.registry.ShippingRegistry.SELL_VALUE;

@HFQuest("friendship.liara.meals")
public class QuestLiara15KMeals extends QuestFriendshipStore {
    public QuestLiara15KMeals() {
        super(HFNPCs.CAFE_OWNER, 15000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.LIARA_12K);
    }

    @Override
    protected Quest getQuest() {
        return Quests.SELL_MEALS;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected NonNullList<ItemStack> getRewardStacks(EntityPlayer player) {
        NonNullList<ItemStack> stacks = NonNullList.create();
        for (int i = 0; i < 3; i++) {
            Random rand = new Random(HFApi.calendar.getDate(player.world).hashCode() + i);
            List<Recipe> list = new ArrayList<>(Recipe.REGISTRY.values());
            ItemStack stack = ItemStack.EMPTY;
            while (stack.isEmpty() || !stack.hasTagCompound()) {
                stack = CookingHelper.makeRecipe(list.get(rand.nextInt(list.size())));
            }

            stack.getTagCompound().setLong(SELL_VALUE, 0L);
            stacks.add(stack);
        }

        return stacks;
    }
}
