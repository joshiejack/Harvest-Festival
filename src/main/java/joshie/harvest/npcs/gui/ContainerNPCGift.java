package joshie.harvest.npcs.gui;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.RelationStatus;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.npcs.NPCHelper;
import joshie.harvest.npcs.entity.EntityNPC;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.player.relationships.RelationshipDataServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import static joshie.harvest.api.calendar.Season.WINTER;

public class ContainerNPCGift extends ContainerNPCChat {
    private static final CalendarDate CHRISTMAS = new CalendarDate(25, WINTER, 0);
    private final EnumHand hand;

    public ContainerNPCGift(EntityPlayer player, EntityNPC npc, EnumHand hand, int nextGui) {
        super(player, npc, nextGui, true);
        this.hand = hand;
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        npc.setTalking(null);
        if (!player.world.isRemote && hand != null) {
            if (HFTrackers.getPlayerTrackerFromPlayer(player).getRelationships().gift(player, npc.getNPC(), 0)) {
                ItemStack gift = player.getHeldItem(hand);
                if (gift.isEmpty() || NPCHelper.INSTANCE.getGifts().isBlacklisted(player.world, player, gift)) return;

                NPC theNpc = npc.getNPC();
                RelationshipDataServer relationships = HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).getRelationships();
                //TODO: Reenable in 1.0 when I readd marriage
                /*if (ToolHelper.isBlueFeather(gift)) {
                    relationships.propose(player, theNpc.getUUID());
                } else { */
                CalendarDate today = HFApi.calendar.getDate(player.world);
                int points = theNpc.getGiftValue(gift).getRelationPoints();
                if (!relationships.hasGivenGift(theNpc, RelationStatus.BIRTHDAY_GIFT) && theNpc.getBirthday().isSameDay(today)) {
                    relationships.setHasGivenGift(theNpc, RelationStatus.BIRTHDAY_GIFT);
                    points *= 10;
                } else if (!relationships.hasGivenGift(theNpc, RelationStatus.CHRISTMAS_GIFT) && CHRISTMAS.isSameDay(today)) {
                    relationships.setHasGivenGift(theNpc, RelationStatus.CHRISTMAS_GIFT);
                    points *= 5;
                }

                relationships.gift(player, theNpc, points);
                //}

                npc.setHeldItem(EnumHand.OFF_HAND, gift.splitStack(1));
            }

            //On closure
            if (nextGui != GuiHandler.NEXT_NONE) {
                player.openGui(HarvestFestival.instance, nextGui, player.world, npc.getEntityId(), 0, -1);
            }
        }
    }
}