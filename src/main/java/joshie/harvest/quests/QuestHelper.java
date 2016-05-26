package joshie.harvest.quests;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quest.IQuest;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.generic.ItemHelper;
import joshie.harvest.core.util.generic.IdiotException;
import joshie.harvest.player.quests.QuestData;
import joshie.harvest.quests.packets.PacketQuestDecreaseHeld;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketSetSlot;

import java.util.HashSet;

import static joshie.harvest.core.network.PacketHandler.sendToServer;

public class QuestHelper {
    public static void completeQuest(EntityPlayer player, Quest quest) {
        HFTrackers.getPlayerTracker(player).getQuests().markCompleted(quest, true);
    }

    public static void takeHeldStack(EntityPlayer player, int amount) {
        if (player.worldObj.isRemote) {
            player.inventory.decrStackSize(player.inventory.currentItem, amount);
            sendToServer(new PacketQuestDecreaseHeld(amount));
        } else {
            player.inventory.decrStackSize(player.inventory.currentItem, amount);
            ((EntityPlayerMP) player).connection.sendPacket(new SPacketSetSlot(-1, -1, player.getActiveItemStack()));
        }
    }

    public static HashSet<IQuest> getCurrentQuest(EntityPlayer player) {
        return HFTrackers.getPlayerTracker(player).getQuests().getCurrent();
    }

    /**************************
     * REWARDS
     *****************************/
    public static void rewardGold(EntityPlayer player, long amount) {
        if (player.worldObj.isRemote) {
            throw new IdiotException("Joshie shouldn't be rewarding anyone with gold client side");
        } else {
            HFTrackers.getServerPlayerTracker(player).getStats().addGold((EntityPlayerMP) player, amount);
        }
    }

    public static void rewardRelations(EntityPlayer player, INPC npc, int amount) {
        HFApi.relations.adjustRelationship(player, npc, amount);
    }

    public static void rewardItem(EntityPlayer player, ItemStack stack) {
        ItemHelper.addToPlayerInventory(player, stack);
    }

    public static void markCompleted(EntityPlayer player, IQuest quest) {
        HFTrackers.getPlayerTracker(player).getQuests().markCompleted(quest, false);
    }

    public static void markAvailable(EntityPlayer player, IQuest quest) {
        HFTrackers.getPlayerTracker(player).getQuests().setAvailable(quest);
    }

    public static void markAsCurrent(EntityPlayer player, IQuest quest) {
        HFTrackers.getPlayerTracker(player).getQuests().addAsCurrent(quest);
    }

    public static void setQuestStage(EntityPlayer player, IQuest quest, int stage) {
        QuestData stats = HFTrackers.getPlayerTracker(player).getQuests();
        int previous = stats.getAQuest(quest).getStage();
        stats.setStage(quest, stage);
        quest.onStageChanged(player, previous, stage);
    }

    public static void startQuest(EntityPlayer player, IQuest quest) {
        HFTrackers.getPlayerTracker(player).getQuests().startQuest(quest);
    }
}