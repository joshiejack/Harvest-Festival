package joshie.harvest.quests.town.seeds;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.player.tracking.StackSold;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestTown;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Set;

@HFQuest("seeds.trees2")
public class QuestTrees2 extends QuestTown {
    private static final int START = 0;
    private static final int FINISHED = 1;
    private CalendarDate lastCheck;
    private final String[] crops;
    private int total;

    public QuestTrees2() {
        this.setNPCs(HFNPCs.GS_OWNER);
        this.crops = new String[] { "cropApple", "cropOrange" };
    }

    private int getTotalCrops(CalendarDate date, EntityPlayer player) {
        if (lastCheck != null && date.equals(lastCheck)) return total;
        else {
            total = 0;
            lastCheck = date.copy();
            for (StackSold sold : HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).getTracking().getShipped()) {
                if (InventoryHelper.isOreName(sold.getStack(), crops)) {
                    total += sold.getAmount();
                }
            }

            return total;
        }
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.SELL_TREES);
    }

    @Override
    public boolean isNPCUsed(EntityPlayer player, NPC npc) {
        if (!super.isNPCUsed(player, npc)) return false;
        boolean ret = quest_stage >= FINISHED;
        if (!player.worldObj.isRemote && quest_stage == START) {
            int totalCrops = getTotalCrops(HFApi.calendar.getDate(player.worldObj), player);
            if (totalCrops >= 100) {
                increaseStage(player);
            }
        }

        return ret;
    }

    @Nullable
    @SideOnly(Side.CLIENT)
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, NPC npc) {
        return getLocalized("complete");
    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, NPC npc, boolean wasSneaking) {
        complete(player);
    }
}
