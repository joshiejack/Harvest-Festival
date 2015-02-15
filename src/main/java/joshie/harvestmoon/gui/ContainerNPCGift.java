package joshie.harvestmoon.gui;

import static joshie.harvestmoon.HarvestMoon.handler;

import java.util.HashSet;

import joshie.harvestmoon.helpers.QuestHelper;
import joshie.harvestmoon.npc.EntityNPC;
import joshie.harvestmoon.quests.Quest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerNPCGift extends ContainerBase {
    //The Fridge CAN be null
    private EntityNPC npc;

    public ContainerNPCGift(EntityNPC npc, InventoryPlayer playerInventory) {
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
            ItemStack gift = player.getCurrentEquippedItem();
            handler.getServer().getPlayerData(player).setGifted(npc.getNPC(), npc.getNPC().getGiftValue(gift).getRelationPoints());
            player.inventory.decrStackSize(player.inventory.currentItem, 1);
        }

        npc.getNPC().onContainerClosed(player, npc);
    }
}
