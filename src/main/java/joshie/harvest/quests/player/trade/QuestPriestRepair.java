package joshie.harvest.quests.player.trade;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.base.item.ItemTool;
import joshie.harvest.core.lib.HFSounds;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestTrade;
import joshie.harvest.tools.HFTools;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

import static joshie.harvest.api.core.ITiered.ToolTier.BLESSED;
import static joshie.harvest.core.helpers.SpawnItemHelper.spawnXP;
import static joshie.harvest.npcs.HFNPCs.PRIEST;
import static joshie.harvest.quests.Quests.TOMAS_MEET;


@HFQuest("trade.bless")
public class QuestPriestRepair extends QuestTrade {
    public QuestPriestRepair() {
        setNPCs(PRIEST);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(TOMAS_MEET);
    }

    @Override
    public boolean isNPCUsed(EntityPlayer player, NPCEntity entity) {
        return entity.getNPC() == HFNPCs.PRIEST && isHolding(player);
    }

    @SideOnly(Side.CLIENT)
    @Override
    @SuppressWarnings("ConstantConditions")
    public String getLocalizedScript(EntityPlayer player, NPCEntity entity) {
        long cost = HFApi.quests.hasCompleted(Quests.TOMAS_15K, player) ? 2500 : 5000;
        boolean hasGold = HFTrackers.getPlayerTrackerFromPlayer(player).getStats().getGold() >= cost;
        if (hasGold) {
            ItemStack tool = player.getHeldItemMainhand(); //For translation reasons
            return getLocalized("done", tool.getDisplayName());
        } else {
            return getLocalized("gold", cost);
        }
    }

    @Override
    public void onChatClosed(EntityPlayer player, NPCEntity npc, boolean wasSneaking) {
        long cost = HFApi.quests.hasCompleted(Quests.TOMAS_15K, player) ? 2500 : 5000;
        boolean hasGold = HFTrackers.getPlayerTrackerFromPlayer(player).getStats().getGold() >= cost;
        if (hasGold) {
            complete(player);
            player.world.playSound(player, player.posX, player.posY, player.posZ, HFSounds.BLESS_TOOL, SoundCategory.NEUTRAL, 0.25F, 1F);
            EntityLiving entity = npc.getAsEntity();
            for (int i = 0; i < 32; i++) {
                player.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, entity.posX + player.world.rand.nextFloat() + player.world.rand.nextFloat() - 1F, entity.posY + 0.25D + entity.world.rand.nextFloat() + entity.world.rand.nextFloat(), entity.posZ + player.world.rand.nextFloat() + player.world.rand.nextFloat() - 1F, 0, 0, 0);
            }
        }
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        if (!player.getHeldItemMainhand().isEmpty()) {
            long cost = HFApi.quests.hasCompleted(Quests.TOMAS_15K, player) ? 1000 : 2500;
            ItemStack stack = player.getHeldItemMainhand().copy();
            ItemStack tool = new ItemStack(stack.getItem(), 1, stack.getItemDamage());
            tool.getOrCreateSubCompound("Data").setDouble("Level", stack.getOrCreateSubCompound("Data").getDouble("Level"));
            rewardGold(player, -cost);
            takeHeldStack(player, 1);
            rewardItem(player, tool);
            spawnXP(player.world, (int) player.posX, (int) player.posY, (int) player.posZ, 5);
        }
    }

    private boolean isHolding(EntityPlayer player) {
        ItemStack held = player.getHeldItemMainhand();
        if (!held.isEmpty()) {
            if (held.getItem() instanceof ItemTool) {
                ItemTool tool = ((ItemTool)held.getItem());
                ToolTier tier = tool.getTier(held);
                return held.getItem() != HFTools.WATERING_CAN && tier == BLESSED;
            }
        }

        return false;
    }
}
