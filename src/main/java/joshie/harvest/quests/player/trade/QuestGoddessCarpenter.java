package joshie.harvest.quests.player.trade;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.HFQuests;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestTrade;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

import static joshie.harvest.core.helpers.InventoryHelper.ORE_DICTIONARY;

@HFQuest("trade.carpenter")
public class QuestGoddessCarpenter extends QuestTrade {
    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.YULIF_MEET);
    }

    @Override
    public boolean isNPCUsed(EntityPlayer player, NPC npc) {
        return npc == HFNPCs.GODDESS && InventoryHelper.getHandItemIsIn(player, ORE_DICTIONARY, "logWood", HFQuests.LOGS_CARPENTER) != null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, NPC npc) {
        return getLocalized("thanks");
    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, NPC npc, boolean wasSneaking) {
        if (InventoryHelper.takeItemsIfHeld(player, ORE_DICTIONARY, "logWood", HFQuests.LOGS_CARPENTER) != null) {
            if (HFBuildings.CHEAT_BUILDINGS) rewardItem(player, HFBuildings.CARPENTER.getSpawner());
            else rewardItem(player, HFBuildings.CARPENTER.getBlueprint());
        }
    }
}
