package joshie.harvest.core.handlers;

import joshie.harvest.animals.AnimalTrackerServer;
import joshie.harvest.api.ticking.IDailyTickable;
import joshie.harvest.calendar.CalendarServer;
import joshie.harvest.core.helpers.MCServerHelper;
import joshie.harvest.core.util.HFEvents;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.town.TownTrackerServer;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.calendar.HFCalendar.TICKS_PER_DAY;

@HFEvents
public class EventsHandler {
    @HFEvents
    public static class ClientReset {
        @SideOnly(Side.CLIENT)
        @SubscribeEvent
        public void onOpenGui(GuiOpenEvent event) {
            if (event.getGui() instanceof GuiWorldSelection || event.getGui() instanceof GuiMultiplayer) {
                HFTrackers.resetClient();
            }
        }
    }

    //Server tick for new day
    @SubscribeEvent
    public void onTick(ServerTickEvent event) {
        if (!OfflineTickHandler.BLOCKED) {
            if (event.phase != Phase.END) return;
            World overworld = FMLCommonHandler.instance().getMinecraftServerInstance().worldServers[0];
            if (overworld.getWorldTime() % TICKS_PER_DAY == 1) {
                HFTrackers.<CalendarServer>getCalendar(overworld).newDay(overworld);
                for (PlayerTrackerServer player : HFTrackers.getPlayerTrackers()) {
                    player.newDay();
                }

                for (World world : FMLCommonHandler.instance().getMinecraftServerInstance().worldServers) {
                    EventsHandler.newDay(world);
                }
            }
        }
    }

    //New day
    public static void newDay(final World world) {
        HFTrackers.getTickables(world).newDay(IDailyTickable.Phase.PRE_ANIMALS);
        HFTrackers.<AnimalTrackerServer>getAnimalTracker(world).newDay();
        HFTrackers.getTickables(world).newDay(IDailyTickable.Phase.POST_ANIMALS);
        HFTrackers.<TownTrackerServer>getTownTracker(world).newDay();
        HFTrackers.markDirty(world);
    }

    //Sync data on login
    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.player;
            HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).getStats().setBirthday(FMLCommonHandler.instance().getMinecraftServerInstance().worldServers[0]); //Set birthday to overworld date
            HFTrackers.<CalendarServer>getCalendar(player.worldObj).syncToPlayer(player);
            HFTrackers.<TownTrackerServer>getTownTracker(event.player.worldObj).syncToPlayer(player);
            HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).syncPlayerStats(player);
        }
    }

    @SubscribeEvent
    public void onChangeDimension(PlayerChangedDimensionEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            HFTrackers.<TownTrackerServer>getTownTracker(MCServerHelper.getWorld(event.toDim)).syncToPlayer(event.player); //Resync the town data
        }
    }
}