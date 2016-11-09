package joshie.harvest.npc.greeting;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.IInfoButton;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.quests.Quests;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class GreetingFlowerBuyer implements IInfoButton {
    @Override
    public boolean canDisplay(INPC npc, EntityPlayer player) {
        return HFApi.quests.hasCompleted(Quests.FLOWER_BUYER, player);
    }

    @Override
    public boolean onClicked(INPC inpc, EntityPlayer player) {
        HFApi.quests.completeQuest(Quests.BUY_FLOWER, player);
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public String getLocalizedText(EntityPlayer player, EntityAgeable ageable, INPC npc) {
        EnumHand hand = player.getHeldItemOffhand() != null ? EnumHand.OFF_HAND : player.getHeldItemMainhand() != null ? EnumHand.MAIN_HAND : null;
        if (hand != null) {
            ItemStack held = player.getHeldItem(hand);
            if (held != null && InventoryHelper.startsWith(held, "flower") && held.stackSize >= 1) {
                held.splitStack(1); //Reduce the stack size by one
                if (held.stackSize <= 0) {
                    player.setHeldItem(hand, null);
                }

                return TextHelper.getRandomSpeech(npc, "harvestfestival.npc.jade.buy", 32);
            }
        }

        return TextHelper.getRandomSpeech(npc, "harvestfestival.npc.jade.buyno", 32);
    }
}
