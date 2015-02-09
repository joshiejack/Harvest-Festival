package joshie.harvestmoon.gui;

import static joshie.harvestmoon.HarvestMoon.handler;

import java.util.HashSet;

import joshie.harvestmoon.entities.EntityNPC;
import joshie.harvestmoon.helpers.QuestHelper;
import joshie.harvestmoon.quests.Quest;
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
        npc.setCustomer((EntityPlayer) null);
        HashSet<Quest> quests = QuestHelper.getCurrentQuest(player);
        for (Quest quest : quests) {
            if (quest != null) {
                quest.onClosedChat(player, npc);
            }
        }

        if (!player.worldObj.isRemote) {
            handler.getServer().getPlayerData(player).setTalkedTo(npc.getNPC());
        }
    }
}
