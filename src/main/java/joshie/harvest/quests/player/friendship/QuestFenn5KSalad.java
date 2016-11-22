package joshie.harvest.quests.player.friendship;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.cooking.CookingHelper;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestFriendship;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
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
    protected List<ItemStack> getRewardStacks(EntityPlayer player) {
        List<ItemStack> stacks = new ArrayList<>();
        //Salad
        ItemStack stack = CookingHelper.getRecipe("salad");
        stack.getTagCompound().setLong(SELL_VALUE, 0L);
        stacks.add(stack);

        //Pickled Cucumber
        stack = CookingHelper.getRecipe("cucumber_pickled");
        stack.getTagCompound().setLong(SELL_VALUE, 0L);
        stacks.add(stack);

        //Boiled Spinach
        stack = CookingHelper.getRecipe("spinach_boiled");
        stack.getTagCompound().setLong(SELL_VALUE, 0L);
        stacks.add(stack);
        return stacks;
    }
}
