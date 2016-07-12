package joshie.harvest.quests.trade;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.gathering.HFGathering;
import joshie.harvest.items.HFItems;
import joshie.harvest.quests.Quest;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

import static joshie.harvest.api.core.ITiered.ToolTier.CURSED;
import static joshie.harvest.npc.HFNPCs.PRIEST;
import static joshie.harvest.quests.QuestHelper.*;

public class QuestBless extends Quest {
    private static final ItemStack hoe = HFCrops.HOE.getStack(CURSED);
    private static final ItemStack sickle = HFCrops.SICKLE.getStack(CURSED);
    private static final ItemStack watering = HFCrops.WATERING_CAN.getStack(CURSED);
    private static final ItemStack axe = HFGathering.AXE.getStack(CURSED);
    private static final ItemStack hammer = HFItems.HAMMER.getStack(CURSED);

    public QuestBless() {
        setNPCs(PRIEST);
    }

    @Override
    public boolean canStartQuest(EntityPlayer player, Set<Quest> active, Set<Quest> finished) {
        return isHolding(player, hoe) || isHolding(player, sickle) || isHolding(player, watering) || isHolding(player, axe) || isHolding(player, hammer);
    }

    @Override
    public void claim(EntityPlayer player) {
        ItemStack stack = player.getHeldItemMainhand().copy();
        takeHeldStack(player, 1);
        rewardGold(player, -10000L);
        rewardItem(player, new ItemStack(stack.getItem(), 1, stack.getItemDamage() + 1));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        boolean hasGold = HFTrackers.getPlayerTracker(player).getStats().getGold() >= 10000L;
        boolean hasTool = isHolding(player, hoe) || isHolding(player, sickle) || isHolding(player, watering) || isHolding(player, axe) || isHolding(player, hammer);
        if (hasGold && hasTool) {
            completeQuest(player, this);
            return "accept";
        } else if (hasGold) {
            return "tool";
        } else if (hasTool) {
            return "gold";
        } else return "reject";
    }

    private boolean isHolding(EntityPlayer player, ItemStack stack) {
        return player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == stack.getItem() && player.getHeldItemMainhand().getItemDamage() == stack.getItemDamage();
    }
}
