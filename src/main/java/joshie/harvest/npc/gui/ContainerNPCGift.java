package joshie.harvest.npc.gui;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.NPCRegistry;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.player.relationships.RelationshipDataServer;
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
                if (gift == null || NPCRegistry.INSTANCE.getGifts().isBlacklisted(gift)) return;

                NPC theNpc = npc.getNPC();
                RelationshipDataServer relationships = HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).getRelationships();

                //TODO: Reenable in 1.0 when I readd marriage
                /*if (ToolHelper.isBlueFeather(gift)) {
                    relationships.propose(player, theNpc.getUUID());
                } else { */
                    int points = theNpc.getGiftValue(gift).getRelationPoints();
                    if (!relationships.hasGivenBirthdayGift(theNpc.getUUID())) {
                        CalendarDate today = HFApi.calendar.getDate(player.worldObj);
                        if (today.isSameDay(theNpc.getBirthday())) {
                            relationships.setHasGivenBirthdayGift(theNpc.getUUID());
                            points *= 5;
                        }
                    }

                    relationships.gift(player, theNpc.getUUID(), points);
                //}
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