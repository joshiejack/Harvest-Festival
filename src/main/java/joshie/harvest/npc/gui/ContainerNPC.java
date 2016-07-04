package joshie.harvest.npc.gui;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.quest.IQuest;
import joshie.harvest.quests.QuestHelper;
import joshie.harvest.core.util.ContainerBase;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.entity.AbstractEntityNPC;
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
        HashSet<IQuest> quests = QuestHelper.getCurrentQuest(player);
        for (IQuest quest : quests) {
            if (quest != null) {
                quest.onClosedChat(player, npc);
            }
        }

        if (!player.worldObj.isRemote) {
            HFApi.relations.talkTo(player, npc.getRelatable());
        }

        if (npc.getNPC() == HFNPCs.GODDESS) {
            npc.setDead();
        }
    }
}