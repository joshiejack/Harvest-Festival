package joshie.harvest.npc.gui;

import java.util.HashSet;

import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quest.IQuest;
import joshie.harvest.core.handlers.HFTracker;
import joshie.harvest.core.helpers.QuestHelper;
import joshie.harvest.core.helpers.ToolHelper;
import joshie.harvest.core.util.ContainerBase;
import joshie.harvest.npc.entity.EntityNPC;
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
        npc.setTalking((EntityPlayer) null);
        HashSet<IQuest> quests = QuestHelper.getCurrentQuest(player);
        for (IQuest quest : quests) {
            if (quest != null) {
                quest.onClosedChat(player, npc);
            }
        }

        if (!player.worldObj.isRemote) {
            ItemStack gift = player.getCurrentEquippedItem();
            INPC theNpc = npc.getNPC();
            int points = theNpc.getGiftValue(gift).getRelationPoints();
            ICalendarDate today = HFTracker.getCalendar().getDate();
            ICalendarDate birthday = theNpc.getBirthday();
            if (today.getSeason() == birthday.getSeason() && today.getDay() == birthday.getDay()) {
                points *= 5;
            }

            if (ToolHelper.isBlueFeather(gift)) {
                HFTracker.getPlayerTracker(player).getRelationships().propose(theNpc);
            }

            HFTracker.getPlayerTracker(player).getRelationships().gift(theNpc, points);
            player.inventory.decrStackSize(player.inventory.currentItem, 1);
        }
    }
}
