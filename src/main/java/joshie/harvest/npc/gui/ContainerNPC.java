package joshie.harvest.npc.gui;

import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.util.ContainerBase;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.entity.AbstractEntityNPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.quests.QuestHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

import java.util.HashSet;

public class ContainerNPC extends ContainerBase {
    //The Fridge CAN be null
    private AbstractEntityNPC npc;

    public ContainerNPC(AbstractEntityNPC npc, InventoryPlayer playerInventory) {
        this.npc = npc;
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        npc.setTalking(null);
        HashSet<Quest> quests = QuestHelper.getCurrentQuest(player);
        for (Quest quest : quests) {
            if (quest != null) {
                quest.onClosedChat(player, npc, npc.getNPC());
            }
        }

        if (!player.worldObj.isRemote) {
            HFTrackers.getPlayerTracker(player).getRelationships().talkTo(player, npc.getRelatable());
        }

        if (npc.getNPC() == HFNPCs.GODDESS) {
            npc.setDead();
        }
    }
}