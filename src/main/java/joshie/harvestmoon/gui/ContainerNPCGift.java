package joshie.harvestmoon.gui;

import java.util.HashSet;

import joshie.harvestmoon.calendar.CalendarDate;
import joshie.harvestmoon.helpers.CalendarHelper;
import joshie.harvestmoon.helpers.QuestHelper;
import joshie.harvestmoon.helpers.RelationsHelper;
import joshie.harvestmoon.npc.EntityNPC;
import joshie.harvestmoon.npc.NPC;
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
            NPC theNpc = npc.getNPC();
            int points = theNpc.getGiftValue(gift).getRelationPoints();
            CalendarDate today = CalendarHelper.getServerDate();
            CalendarDate birthday = theNpc.getBirthday();
            if (today.getSeason() == birthday.getSeason() && today.getDay() == birthday.getDay()) {
                points *= 5;
            }

            RelationsHelper.setGifted(player, npc.getNPC(), points);
            player.inventory.decrStackSize(player.inventory.currentItem, 1);
        }

        npc.getNPC().onContainerClosed(player, npc);
    }
}
