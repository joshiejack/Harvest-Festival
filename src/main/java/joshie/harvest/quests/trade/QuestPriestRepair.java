package joshie.harvest.quests.trade;

import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.base.item.ItemTool;
import joshie.harvest.core.lib.HFSounds;
import joshie.harvest.tools.HFTools;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.api.core.ITiered.ToolTier.BLESSED;
import static joshie.harvest.core.helpers.SpawnItemHelper.spawnXP;
import static joshie.harvest.npc.HFNPCs.PRIEST;


@HFQuest("trade.bless")
public class QuestPriestRepair extends QuestTrade {
    public QuestPriestRepair() {
        setNPCs(PRIEST);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        boolean hasGold = HFTrackers.getPlayerTrackerFromPlayer(player).getStats().getGold() >= 500;
        boolean hasTool = isHolding(player);
        if (hasGold && hasTool) {
            ItemStack tool = player.getHeldItemMainhand(); //For translation reasons
            return getLocalized("done", tool.getDisplayName());
        } else if (hasTool) {
            return getLocalized("gold");
        } else return null;
    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, INPC npc) {
        boolean hasGold = HFTrackers.getPlayerTrackerFromPlayer(player).getStats().getGold() >= 500;
        boolean hasTool = isHolding(player);
        if (hasGold && hasTool) {
            complete(player);
            player.worldObj.playSound(player, player.posX, player.posY, player.posZ, HFSounds.BLESS_TOOL, SoundCategory.NEUTRAL, 0.25F, 1F);
            for (int i = 0; i < 32; i++) {
                player.worldObj.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, entity.posX + player.worldObj.rand.nextFloat() + player.worldObj.rand.nextFloat() - 1F, entity.posY + 0.25D + entity.worldObj.rand.nextFloat() + entity.worldObj.rand.nextFloat(), entity.posZ + player.worldObj.rand.nextFloat() + player.worldObj.rand.nextFloat() - 1F, 0, 0, 0);
            }
        }
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        if (player.getHeldItemMainhand() != null) {
            ItemStack stack = player.getHeldItemMainhand().copy();
            ItemStack tool = new ItemStack(stack.getItem(), 1, stack.getItemDamage());
            tool.getSubCompound("Data", true).setDouble("Level", stack.getSubCompound("Data", true).getDouble("Level"));
            rewardGold(player, -500L);
            takeHeldStack(player, 1);
            rewardItem(player, tool);
            spawnXP(player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ, 5);
        }
    }

    private boolean isHolding(EntityPlayer player) {
        ItemStack held = player.getHeldItemMainhand();
        if (held != null) {
            if (held.getItem() instanceof ItemTool) {
                ItemTool tool = ((ItemTool)held.getItem());
                ToolTier tier = tool.getTier(held);
                return held.getItem() != HFTools.WATERING_CAN && tier == BLESSED;
            }
        }

        return false;
    }
}
