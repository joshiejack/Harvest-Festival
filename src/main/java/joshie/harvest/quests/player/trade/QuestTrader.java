package joshie.harvest.quests.player.trade;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.Size;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import joshie.harvest.quests.base.QuestTrade;
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
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (isHoldingInEitherHand(player, Sizeable.EGG)) {
            return getLocalized("egg");
        } else if (isHoldingInEitherHand(player, Sizeable.MILK)) {
            return getLocalized("milk");
        } else if (isHoldingInEitherHand(player, Sizeable.WOOL)) {
            return getLocalized("wool");
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
                Sizeable sizeable = HFAnimals.ANIMAL_PRODUCT.getEnumFromStack(held);
                Item item = sizeable == Sizeable.EGG ? Items.EGG : sizeable == Sizeable.MILK ? Items.MILK_BUCKET : WOOL;
                rewardItem(player, new ItemStack(item, amount));
            }
        }
    }

    private boolean isHoldingAnyAtAll(EntityPlayer player) {
        return isHoldingAny(player, EnumHand.MAIN_HAND) || isHoldingAny(player, EnumHand.OFF_HAND);
    }

    private boolean isHoldingAny(EntityPlayer player, EnumHand hand) {
        return isHolding(player.getHeldItem(hand), Sizeable.EGG) || isHolding(player.getHeldItem(hand), Sizeable.MILK) || isHolding(player.getHeldItem(hand), Sizeable.WOOL);
    }

    private boolean isHolding(ItemStack holding, Sizeable sizeable) {
        return holding != null && holding.getItem() == HFAnimals.ANIMAL_PRODUCT && HFAnimals.ANIMAL_PRODUCT.getEnumFromStack(holding) == sizeable;
    }

    private boolean isHoldingInEitherHand(EntityPlayer player, Sizeable sizeable) {
        return isHolding(player.getHeldItem(EnumHand.MAIN_HAND), sizeable) || isHolding(player.getHeldItem(EnumHand.OFF_HAND), sizeable);
    }
}
