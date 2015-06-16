package joshie.harvest.core.helpers;

import static joshie.harvest.core.network.PacketHandler.sendToClient;
import static joshie.harvest.core.network.PacketHandler.sendToServer;

import java.util.HashSet;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quest.IQuest;
import joshie.harvest.core.handlers.HFTracker;
import joshie.harvest.core.helpers.generic.ItemHelper;
import joshie.harvest.core.network.PacketSyncGold;
import joshie.harvest.core.network.quests.PacketQuestDecreaseHeld;
import joshie.harvest.core.util.generic.IdiotException;
import joshie.harvest.player.PlayerStats;
import joshie.harvest.quests.Quest;
import joshie.harvest.quests.QuestStats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2FPacketSetSlot;

public class QuestHelper {
    public static void completeQuest(EntityPlayer player, Quest quest) {
        HFTracker.getPlayerTracker(player).getQuests().markCompleted(quest, true);
    }

    public static void takeHeldStack(EntityPlayer player, int amount) {
        if (player.worldObj.isRemote) {
            player.inventory.decrStackSize(player.inventory.currentItem, amount);
            sendToServer(new PacketQuestDecreaseHeld(amount));
        } else {
            player.inventory.decrStackSize(player.inventory.currentItem, amount);
            ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S2FPacketSetSlot(-1, -1, player.getCurrentEquippedItem()));
        }
    }

    public static HashSet<IQuest> getCurrentQuest(EntityPlayer player) {
        return HFTracker.getPlayerTracker(player).getQuests().getCurrent();
    }

    /************************** REWARDS *****************************/
    public static void rewardGold(EntityPlayer player, long amount) {
        if (player.worldObj.isRemote) {
            throw new IdiotException("Joshie shouldn't be rewarding anyone with gold client side");
        } else {
            PlayerStats stats = HFTracker.getPlayerTracker(player).getStats();
            stats.addGold(amount);
            sendToClient(new PacketSyncGold(stats.getGold()), (EntityPlayerMP) player);
        }
    }

    public static void rewardRelations(EntityPlayer player, INPC npc, int amount) {
        HFTracker.getPlayerTracker(player).getRelationships().affectRelationship(npc, amount);
    }

    public static void rewardItem(EntityPlayer player, ItemStack stack) {
        ItemHelper.addToPlayerInventory(player, stack);
    }

    public static void markCompleted(EntityPlayer player, IQuest quest) {
        HFTracker.getPlayerTracker(player).getQuests().markCompleted(quest, false);
    }

    public static void markAvailable(EntityPlayer player, IQuest quest) {
        HFTracker.getPlayerTracker(player).getQuests().setAvailable(quest);
    }

    public static void markAsCurrent(EntityPlayer player, IQuest quest) {
        HFTracker.getPlayerTracker(player).getQuests().addAsCurrent(quest);
    }

    public static void setQuestStage(EntityPlayer player, IQuest quest, int stage) {
        QuestStats stats = HFTracker.getPlayerTracker(player).getQuests();
        int previous = stats.getAQuest(quest).getStage();
        stats.setStage(quest, stage);
        quest.onStageChanged(player, previous, stage);
    }

    public static void startQuest(EntityPlayer player, IQuest quest) {
        HFTracker.getPlayerTracker(player).getQuests().startQuest(quest);
    }
}
