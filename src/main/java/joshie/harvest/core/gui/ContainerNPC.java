package joshie.harvest.core.gui;

import java.util.HashSet;

import joshie.harvest.api.quest.IQuest;
import joshie.harvest.core.helpers.QuestHelper;
import joshie.harvest.core.helpers.RelationsHelper;
import joshie.harvest.init.HFNPCs;
import joshie.harvest.npc.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerNPC extends ContainerBase {
    //The Fridge CAN be null
    private EntityNPC npc;

    public ContainerNPC(EntityNPC npc, InventoryPlayer playerInventory) {
        this.npc = npc;
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        npc.setTalking((EntityPlayer) null);
        HashSet<IQuest> quests = QuestHelper.getCurrentQuest(player);
        for (IQuest quest : quests) {
            if (quest != null) {
                quest.onClosedChat(player, npc);
            }
        }

        if (!player.worldObj.isRemote) {
            RelationsHelper.setTalkedTo(player, npc);
        }
        
        if (npc.getNPC() == HFNPCs.goddess) {
            npc.setDead();
        }
    }
}
