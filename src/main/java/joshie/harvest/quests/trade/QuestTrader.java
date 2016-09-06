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
        if (isHolding(player, HFAnimals.EGG)) {
            complete(player);
            return "egg";
        } else if (isHolding(player, HFAnimals.MILK)) {
            complete(player);
            return "milk";
        } else if (isHolding(player, HFAnimals.WOOL)) {
            complete(player);
            return "wool";
        } else return null;
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        ItemStack stack = player.getHeldItemMainhand().copy();
        takeHeldStack(player, stack.stackSize); //Take everything
        Size size = HFApi.sizeable.getSize(stack);
        int amount = stack.stackSize;
        if (size == Size.MEDIUM) amount *= 2;
        else if (size == Size.LARGE) amount *= 3;
        Sizeable sizeable = HFCore.SIZEABLE.getObjectFromStack(stack);
        Item item = sizeable == HFAnimals.EGG ? Items.EGG : sizeable == HFAnimals.MILK ? Items.MILK_BUCKET : WOOL;
        rewardItem(player, new ItemStack(item, amount));
    }

    private boolean isHolding(EntityPlayer player, Sizeable sizeable) {
        return player.getHeldItemMainhand() != null && sizeable.matches(player.getHeldItemMainhand());
    }
}
