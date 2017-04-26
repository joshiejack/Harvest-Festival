package joshie.harvest.quests.player.friendship;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestFriendship;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.Set;

import static joshie.harvest.core.registry.ShippingRegistry.SELL_VALUE;

@HFQuest("friendship.fenn.salad")
public class QuestFenn5KSalad extends QuestFriendship {
    public QuestFenn5KSalad() {
        super(HFNPCs.CLOCKMAKER_CHILD, 5000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.FENN_MEET);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected NonNullList<ItemStack> getRewardStacks(EntityPlayer player) {
        NonNullList<ItemStack> stacks = NonNullList.create();
        //Salad
        ItemStack stack = HFCooking.MEAL.getCreativeStack(Meal.SALAD);
        stack.getTagCompound().setLong(SELL_VALUE, 0L);
        stacks.add(stack);

        //Pickled Cucumber
        stack = HFCooking.MEAL.getCreativeStack(Meal.CUCUMBER_PICKLED);
        stack.getTagCompound().setLong(SELL_VALUE, 0L);
        stacks.add(stack);

        //Boiled Spinach
        stack = HFCooking.MEAL.getCreativeStack(Meal.SPINACH_BOILED);
        stack.getTagCompound().setLong(SELL_VALUE, 0L);
        stacks.add(stack);
        return stacks;
    }
}
