package joshie.harvest.npc.gui;

import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.util.ContainerBase;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.quests.QuestHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

import java.util.Set;

import static joshie.harvest.api.quests.Quest.EventsHandled.CLOSEDCHAT;

public class ContainerNPC extends ContainerBase {
    //The Fridge CAN be null
    private EntityNPC npc;

    public ContainerNPC(EntityNPC npc, InventoryPlayer playerInventory) {
        this.npc = npc;
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        npc.setTalking(null);
        Set<Quest> quests = QuestHelper.getHandledQuests(player, CLOSEDCHAT);
        for (Quest quest : quests) {
            quest.onClosedChat(player, npc, npc.getNPC());
        }

        if (!player.worldObj.isRemote) {
            HFTrackers.getPlayerTracker(player).getRelationships().talkTo(player, npc.getRelatable());
        }
    }
}