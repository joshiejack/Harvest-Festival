package joshie.harvest.core.handlers;

import joshie.harvest.calendar.data.CalendarServer;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.MCServerHelper;
import joshie.harvest.core.util.annotations.HFEvents;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.town.tracker.TownTrackerServer;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@HFEvents
@SuppressWarnings("unused")
public class SyncHandler {
    @HFEvents
    @SuppressWarnings("unused")
    public static class ClientReset {
        @SideOnly(Side.CLIENT)
        @SubscribeEvent
        public void onOpenGui(GuiOpenEvent event) {
            if (event.getGui() instanceof GuiWorldSelection || event.getGui() instanceof GuiMultiplayer) {
                HFTrackers.resetClient();
            }
        }
    }


    //Sync data on login
    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.player;
            HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).getStats().setBirthday(player.world); //Set birthday to overworld date
            HFTrackers.<CalendarServer>getCalendar(player.world).syncToPlayer(player);
            HFTrackers.<TownTrackerServer>getTowns(event.player.world).syncToPlayer(player);
            HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).syncPlayerStats(player);
        }
    }

    @SubscribeEvent
    public void onChangeDimension(PlayerChangedDimensionEvent event) {
        if (event.player instanceof EntityPlayerMP && event.toDim == 0) {
            HFTrackers.<TownTrackerServer>getTowns(MCServerHelper.getWorld(event.toDim)).syncToPlayer((EntityPlayerMP)event.player); //Resync the town data
        }
    }
}