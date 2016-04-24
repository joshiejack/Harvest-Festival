package joshie.harvest.npc.gui;

import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.ToolHelper;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerNPCGift extends ContainerNPCBase {
    //The Fridge CAN be null
    private EntityNPC npc;

    public ContainerNPCGift(EntityNPC npc, InventoryPlayer playerInventory) {
        super(npc, playerInventory);
        this.npc = npc;
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);

        if (!player.worldObj.isRemote) {
            ItemStack gift = player.getCurrentEquippedItem();
            INPC theNpc = npc.getNPC();
            int points = theNpc.getGiftValue(gift).getRelationPoints();
            ICalendarDate today = HFTrackers.getCalendar().getDate();
            ICalendarDate birthday = theNpc.getBirthday();
            if (today.getSeason() == birthday.getSeason() && today.getDay() == birthday.getDay()) {
                points *= 5;
            }

            if (ToolHelper.isBlueFeather(gift)) {
                HFTrackers.getPlayerTracker(player).getRelationships().propose(player, theNpc);
            }

            HFTrackers.getPlayerTracker(player).getRelationships().gift(player, theNpc, points);
            player.inventory.decrStackSize(player.inventory.currentItem, 1);
        }
        
        //Kill the goddess
        if (npc.getNPC() == HFNPCs.goddess) {
            npc.setDead();
        }
    }
}
