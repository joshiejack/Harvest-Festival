package joshie.harvest.quests.town.trade;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.quests.base.QuestDummyTown;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import java.util.Random;
import java.util.Set;

@HFQuest("trader.flowers")
public class QuestFlowerBuyingDo extends QuestDummyTown {
    private CalendarDate lastCheck;
    private int value;

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return false;
    }

    private int getValue(CalendarDate date, Random rand) {
        if (lastCheck != null && date.equals(lastCheck)) return value;
        else {
            lastCheck = date.copy();
            value = 1 + rand.nextInt(25);
            return value;
        }
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        EnumHand hand = !player.getHeldItemOffhand().isEmpty() ? EnumHand.OFF_HAND : !player.getHeldItemMainhand().isEmpty() ? EnumHand.MAIN_HAND : null;
        if (hand != null) {
            ItemStack held = player.getHeldItem(hand);
            if (InventoryHelper.startsWith(held, "flower") && held.getCount() >= 1) {
                held.splitStack(1); //Reduce the stack size by one
                rewardGold(player, getValue(HFApi.calendar.getDate(player.world), player.world.rand));
            }
        }
    }
}
