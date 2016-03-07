package joshie.harvest.shops.gui;

import java.util.HashSet;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.quest.IQuest;
import joshie.harvest.core.helpers.QuestHelper;
import joshie.harvest.core.util.ContainerBase;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerNPCShop extends ContainerBase {
    private EntityNPC npc;

    public ContainerNPCShop(EntityNPC npc, InventoryPlayer playerInventory) {
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
            HFApi.RELATIONS.talkTo(player, npc.getRelatable());
        }
    }
}
