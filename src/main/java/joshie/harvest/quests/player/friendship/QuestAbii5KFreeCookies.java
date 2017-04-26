package joshie.harvest.quests.player.friendship;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestFriendship;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.Set;

import static joshie.harvest.core.registry.ShippingRegistry.SELL_VALUE;

@HFQuest("friendship.abii.cookies")
public class QuestAbii5KFreeCookies extends QuestFriendship {
    public QuestAbii5KFreeCookies() {
        super(HFNPCs.DAUGHTER_CHILD, 5000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.ABI_MEET);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected NonNullList<ItemStack> getRewardStacks(EntityPlayer player) {
        NonNullList<ItemStack> stacks = NonNullList.create();
        //Normal Cookies
        ItemStack stack = HFCooking.MEAL.getCreativeStack(Meal.COOKIES);
        stack.getTagCompound().setLong(SELL_VALUE, 0L);
        stacks.add(stack);

        //Chocolate Cookies
        stack = HFCooking.MEAL.getCreativeStack(Meal.COOKIES_CHOCOLATE);
        stack.getTagCompound().setLong(SELL_VALUE, 0L);
        stacks.add(stack);

        //Vanilla Cookies
        stacks.add(new ItemStack(Items.COOKIE));
        return stacks;
    }
}
