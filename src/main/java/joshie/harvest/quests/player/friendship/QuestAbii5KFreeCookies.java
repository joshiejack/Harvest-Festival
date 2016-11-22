package joshie.harvest.quests.player.friendship;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.cooking.CookingHelper;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestFriendship;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
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
    protected List<ItemStack> getRewardStacks(EntityPlayer player) {
        List<ItemStack> stacks = new ArrayList<>();
        //Normal Cookies
        ItemStack stack = CookingHelper.getRecipe("cookies");
        stack.getTagCompound().setLong(SELL_VALUE, 0L);
        stacks.add(stack);

        //Chocolate Cookies
        stack = CookingHelper.getRecipe("cookies_chocolate");
        stack.getTagCompound().setLong(SELL_VALUE, 0L);
        stacks.add(stack);

        //Vanilla Cookies
        stacks.add(new ItemStack(Items.COOKIE));
        return stacks;
    }
}
