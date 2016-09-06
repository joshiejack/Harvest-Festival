package joshie.harvest.quests.trade;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.core.lib.HFQuests;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

import static joshie.harvest.core.helpers.InventoryHelper.ORE_DICTIONARY;
import static joshie.harvest.npc.HFNPCs.GODDESS;

@HFQuest("trade.carpenter")
public class QuestGoddessCarpenter extends QuestTrade {
    public QuestGoddessCarpenter() {
        setNPCs(GODDESS);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(HFQuests.TUTORIAL_CARPENTER);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        return InventoryHelper.isHolding(player, ORE_DICTIONARY, "logWood", 64) ? "thanks" : null;
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        InventoryHelper.takeItemsIfHeld(player, ORE_DICTIONARY, "logWood", 64);
        rewardItem(player, HFBuildings.CARPENTER.getBlueprint());
    }
}
