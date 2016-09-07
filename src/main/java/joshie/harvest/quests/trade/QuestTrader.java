package joshie.harvest.quests.trade;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.lib.Sizeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.npc.HFNPCs.TRADER;

@HFQuest("trade.vanilla")
public class QuestTrader extends QuestTrade {
    private static final Item WOOL = Item.getItemFromBlock(Blocks.WOOL);

    public QuestTrader() {
        setNPCs(TRADER);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (isHoldingInEitherHand(player, HFAnimals.EGG)) {
            return "egg";
        } else if (isHoldingInEitherHand(player, HFAnimals.MILK)) {
            return "milk";
        } else if (isHoldingInEitherHand(player, HFAnimals.WOOL)) {
            return "wool";
        } else return null;
    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (isHoldingAnyAtAll(player)) {
            EnumHand hand = isHoldingAny(player, EnumHand.MAIN_HAND) ? EnumHand.MAIN_HAND: EnumHand.OFF_HAND;
            if (player.getHeldItem(hand) != null) {
                ItemStack held = player.getHeldItem(hand).copy(); //Ignore
                takeHeldStack(player, held.stackSize);
                Size size = HFApi.sizeable.getSize(held);
                int amount = held.stackSize;
                if (size == Size.MEDIUM) amount *= 2;
                else if (size == Size.LARGE) amount *= 3;
                Sizeable sizeable = HFCore.SIZEABLE.getObjectFromStack(held);
                Item item = sizeable == HFAnimals.EGG ? Items.EGG : sizeable == HFAnimals.MILK ? Items.MILK_BUCKET : WOOL;
                rewardItem(player, new ItemStack(item, amount));
            }
        }
    }

    private boolean isHoldingAnyAtAll(EntityPlayer player) {
        return isHoldingAny(player, EnumHand.MAIN_HAND) || isHoldingAny(player, EnumHand.OFF_HAND);
    }

    private boolean isHoldingAny(EntityPlayer player, EnumHand hand) {
        return isHolding(player, HFAnimals.EGG, hand) || isHolding(player, HFAnimals.MILK, hand) || isHolding(player, HFAnimals.WOOL, hand);
    }

    private boolean isHolding(EntityPlayer player, Sizeable sizeable, EnumHand hand) {
        return player.getHeldItem(hand) != null && sizeable.matches(player.getHeldItem(hand));
    }

    private boolean isHoldingInEitherHand(EntityPlayer player, Sizeable sizeable) {
        return isHolding(player, sizeable, EnumHand.MAIN_HAND) || isHolding(player, sizeable, EnumHand.OFF_HAND);
    }
}
