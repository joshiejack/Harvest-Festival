package joshie.harvest.npc.gui;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.NPCRegistry;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.tools.ToolHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class ContainerNPCGift extends ContainerNPCChat {
    public ContainerNPCGift(EntityPlayer player, EntityNPC npc, EnumHand hand, int nextGui) {
        super(player, npc, hand, nextGui);
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        if (!player.worldObj.isRemote) {
            if (HFTrackers.getPlayerTrackerFromPlayer(player).getRelationships().gift(player, npc.getNPC().getUUID(), 0)) {
                ItemStack gift = player.getHeldItem(hand);
                if (NPCRegistry.INSTANCE.getGifts().isBlacklisted(gift)) return;

                NPC theNpc = npc.getNPC();
                int points = theNpc.getGiftValue(gift).getRelationPoints();
                CalendarDate today = HFApi.calendar.getDate(player.worldObj);
                if (today.isSameDay(theNpc.getBirthday())) {
                    points *= 5;
                }

                if (ToolHelper.isBlueFeather(gift)) {
                    HFTrackers.getPlayerTrackerFromPlayer(player).getRelationships().propose(player, theNpc.getUUID());
                }

                HFTrackers.getPlayerTrackerFromPlayer(player).getRelationships().gift(player, theNpc.getUUID(), points);
                if (gift != null) {
                    gift.splitStack(1);
                    if (gift.stackSize <= 0) {
                        player.setHeldItem(hand, null);
                    }
                }
            }
        }
    }
}