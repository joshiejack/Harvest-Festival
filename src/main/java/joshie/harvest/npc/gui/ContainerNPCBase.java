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

public class ContainerNPCBase extends ContainerBase {
    protected int nextGui = -1;
    protected EntityNPC npc;

    public ContainerNPCBase(EntityNPC npc, InventoryPlayer playerInventory) {
        this.npc = npc;
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        if (nextGui == -1) npc.setTalking(null);
        Set<Quest> quests = QuestHelper.getHandledQuests(player, CLOSEDCHAT);
        for (Quest quest : quests) {
            quest.onClosedChat(player, npc, npc.getNPC());
        }

        if (!player.worldObj.isRemote) {
            HFTrackers.getPlayerTracker(player).getRelationships().talkTo(player, npc.getRelatable());
        }
    }
}
