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
import net.minecraft.util.EnumHand;

public class ContainerNPCGift extends ContainerNPCBase {
    //The Fridge CAN be null
    private EntityNPC npc;
    private EnumHand hand;

    public ContainerNPCGift(EntityNPC npc, InventoryPlayer playerInventory, EnumHand hand) {
        super(npc, playerInventory);
        this.npc = npc;
        this.hand = hand;
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);

        if (!player.worldObj.isRemote) {
            new Exception().printStackTrace();
            ItemStack gift = player.getHeldItem(hand);
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
        if (npc.getNPC() == HFNPCs.GODDESS) {
            npc.setDead();
        }
    }
}