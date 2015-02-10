package joshie.harvestmoon.helpers;

import static joshie.harvestmoon.HarvestMoon.handler;
import static joshie.harvestmoon.network.PacketHandler.sendToClient;
import static joshie.harvestmoon.network.PacketHandler.sendToServer;

import java.util.HashSet;

import joshie.harvestmoon.entities.npc.NPC;
import joshie.harvestmoon.network.PacketSyncGold;
import joshie.harvestmoon.network.quests.PacketQuestCompleted;
import joshie.harvestmoon.network.quests.PacketQuestDecreaseHeld;
import joshie.harvestmoon.player.PlayerDataServer;
import joshie.harvestmoon.quests.Quest;
import joshie.harvestmoon.util.generic.IdiotException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S2FPacketSetSlot;

public class QuestHelper {
    public static void completeQuest(EntityPlayer player, Quest quest) {
        if (player.worldObj.isRemote) {
            handler.getClient().getPlayerData().getQuests().markCompleted(quest);
            sendToServer(new PacketQuestCompleted(quest, true));
        } else {
            handler.getServer().getPlayerData(player).getQuests().markCompleted(quest);
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

    public static HashSet<Quest> getCurrentQuest(EntityPlayer player) {
        HashSet<Quest> quest = null;
        if (player.worldObj.isRemote) {
            quest = handler.getClient().getPlayerData().getQuests().getCurrent();
        } else {
            quest = handler.getServer().getPlayerData(player).getQuests().getCurrent();
        }

        return quest;
    }

    /************************** REWARDS *****************************/
    public static void rewardGold(EntityPlayer player, int amount) {
        if (player.worldObj.isRemote) {
            throw new IdiotException("Joshie shouldn't be rewarding anyone with gold client side");
        } else {
            PlayerDataServer data = handler.getServer().getPlayerData(player);
            data.addGold(100);
            sendToClient(new PacketSyncGold(data.getGold()), (EntityPlayerMP) player);
        }
    }

    public static void rewardRelations(EntityPlayer player, NPC npc, int amount) {
        if (player.worldObj.isRemote) {
            throw new IdiotException("Joshie shouldn't be rewarding anyone with gold client side");
        } else {
            handler.getServer().getPlayerData(player).affectRelationship(npc, amount);
        }
    }
}
