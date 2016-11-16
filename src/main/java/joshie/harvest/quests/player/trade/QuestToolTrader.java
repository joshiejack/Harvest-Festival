package joshie.harvest.quests.player.trade;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.core.helpers.InventoryHelper.SearchType;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestTrade;
import joshie.harvest.tools.HFTools;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

import static joshie.harvest.api.core.ITiered.ToolTier.BASIC;
import static joshie.harvest.core.helpers.InventoryHelper.SPECIAL;
import static joshie.harvest.npc.HFNPCs.FLOWER_GIRL;

@HFQuest("trade.tools")
public class QuestToolTrader extends QuestTrade {
    public QuestToolTrader() {
        setNPCs(FLOWER_GIRL);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.JADE_MEET) && !finished.contains(Quests.DANIERU_MEET);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (InventoryHelper.getHandItemIsIn(player, SPECIAL, SearchType.HOE) != null) {
            return getLocalized("hoe");
        } else if (InventoryHelper.getHandItemIsIn(player, SPECIAL, SearchType.BUCKET) != null) {
            return getLocalized("wateringcan");
        } else if (InventoryHelper.getHandItemIsIn(player, SPECIAL, SearchType.SHEARS) != null) {
            return getLocalized("sickle");
        } else if (player.worldObj.rand.nextFloat() <= 0.05F) return getLocalized("reminder");

         return null;
    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, INPC npc, boolean isSneaking) {
        if (InventoryHelper.getHandItemIsIn(player, SPECIAL, SearchType.HOE) != null ||
                InventoryHelper.getHandItemIsIn(player, SPECIAL, SearchType.BUCKET) != null ||
                InventoryHelper.getHandItemIsIn(player, SPECIAL, SearchType.SHEARS) != null) {
            complete(player);
        }
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        if (InventoryHelper.takeItemsIfHeld(player, SPECIAL, SearchType.HOE) != null) {
            rewardItem(player, HFTools.HOE.getStack(BASIC));
        } else if (InventoryHelper.takeItemsIfHeld(player, SPECIAL, SearchType.BUCKET) != null) {
            rewardItem(player, HFTools.WATERING_CAN.getStack(BASIC));
        } else if (InventoryHelper.takeItemsIfHeld(player, SPECIAL, SearchType.SHEARS) != null) {
            rewardItem(player, HFTools.SICKLE.getStack(BASIC));
        }
    }
}
