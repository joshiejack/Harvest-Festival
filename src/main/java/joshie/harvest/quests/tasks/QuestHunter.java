package joshie.harvest.quests.tasks;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.tools.HFTools;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import static joshie.harvest.api.core.ITiered.ToolTier.BLESSED;
import static joshie.harvest.core.helpers.InventoryHelper.ITEM_STACK;

@HFQuest("random.hunter")
public class QuestHunter extends Quest {
    public QuestHunter() {
        setNPCs(HFNPCs.POULTRY);
        setTownQuest();
    }

    @Override
    @Nullable
    @SideOnly(Side.CLIENT)
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (InventoryHelper.getHandItemIsIn(player, ITEM_STACK, HFCrops.STRAWBERRY.getCropStack(9), 9) != null) {
            return "ashlee has a script";
        } else return "ashlee wants strawberries";
    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, INPC npc, boolean wasSneaking) {
        System.out.println("We closed the chat on side: " + player.worldObj.isRemote);
        if (InventoryHelper.getHandItemIsIn(player, ITEM_STACK, HFCrops.STRAWBERRY.getCropStack(9), 9) != null) {
            complete(player);
        }
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        rewardItem(player, HFTools.SICKLE.getStack(BLESSED));
    }
}
