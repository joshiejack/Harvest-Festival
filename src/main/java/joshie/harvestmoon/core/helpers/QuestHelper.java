package joshie.harvestmoon.core.helpers;

import static joshie.harvestmoon.core.network.PacketHandler.sendToClient;
import static joshie.harvestmoon.core.network.PacketHandler.sendToServer;

import java.util.HashSet;

import joshie.harvestmoon.api.npc.INPC;
import joshie.harvestmoon.api.quest.IQuest;
import joshie.harvestmoon.core.network.PacketSyncGold;
import joshie.harvestmoon.core.network.quests.PacketQuestCompleted;
import joshie.harvestmoon.core.network.quests.PacketQuestDecreaseHeld;
import joshie.harvestmoon.core.util.generic.IdiotException;
import joshie.harvestmoon.player.PlayerDataServer;
import joshie.harvestmoon.quests.Quest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S2FPacketSetSlot;

public class QuestHelper {
    public static void completeQuest(EntityPlayer player, Quest quest) {
        if (player.worldObj.isRemote) {
            ClientHelper.getPlayerData().getQuests().markCompleted(quest);
            sendToServer(new PacketQuestCompleted(quest, true));
        } else {
            ServerHelper.getPlayerData(player).getQuests().markCompleted(quest);
            sendToServer(new PacketQuestCompleted(quest, false));
        }
    }

    public static void takeHeldStack(EntityPlayer player, int amount) {
        if (player.worldObj.isRemote) {
            player.inventory.decrStackSize(player.inventory.currentItem, amount);
            sendToServer(new PacketQuestDecreaseHeld(10));
        } else {
            player.inventory.decrStackSize(player.inventory.currentItem, amount);
            ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S2FPacketSetSlot(-1, -1, player.getCurrentEquippedItem()));
        }
    }

    public static HashSet<IQuest> getCurrentQuest(EntityPlayer player) {
        HashSet<IQuest> quest = null;
        if (player.worldObj.isRemote) {
            quest = ClientHelper.getPlayerData().getQuests().getCurrent();
        } else {
            quest = ServerHelper.getPlayerData(player).getQuests().getCurrent();
        }

        return quest;
    }

    /************************** REWARDS *****************************/
    public static void rewardGold(EntityPlayer player, long amount) {
        if (player.worldObj.isRemote) {
            throw new IdiotException("Joshie shouldn't be rewarding anyone with gold client side");
        } else {
            PlayerDataServer data = ServerHelper.getPlayerData(player);
            data.addGold(100);
            sendToClient(new PacketSyncGold(data.getGold()), (EntityPlayerMP) player);
        }
    }

    public static void rewardRelations(EntityPlayer player, INPC npc, int amount) {
        if (player.worldObj.isRemote) {
            throw new IdiotException("Joshie shouldn't be rewarding anyone with gold client side");
        } else {
            ServerHelper.getPlayerData(player).affectRelationship(npc, amount);
        }
    }

    public static void markCompleted(EntityPlayer player, IQuest quest) {
        if (!player.worldObj.isRemote) {
            ServerHelper.getPlayerData(player).getQuests().markCompleted(quest);
        } else ClientHelper.getPlayerData().getQuests().markCompleted(quest);
    }

    public static void markAvailable(EntityPlayer player, IQuest quest) {
        if (!player.worldObj.isRemote) {

        } else ClientHelper.getPlayerData().getQuests().setAvailable(quest);
    }

    public static void markAsCurrent(EntityPlayer player, IQuest quest) {
        if (!player.worldObj.isRemote) {

        } else ClientHelper.getPlayerData().getQuests().addAsCurrent(quest);
    }

    public static void setQuestStage(EntityPlayer player, IQuest quest, int stage) {
        if (!player.worldObj.isRemote) {
            ServerHelper.getPlayerData(player).getQuests().setStage(quest, stage);
        } else ClientHelper.getPlayerData().getQuests().setStage(quest, stage);
    }

    public static void startQuest(EntityPlayer player, IQuest quest) {
        if (!player.worldObj.isRemote) {
            ServerHelper.getPlayerData(player).getQuests().startQuest(quest);
        }
    }
}
