package joshie.harvest.core.handlers;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.tracker.AnimalTrackerServer;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.ticking.IDailyTickable;
import joshie.harvest.calendar.data.CalendarServer;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.util.annotations.HFEvents;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.town.tracker.TownTrackerServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

import static joshie.harvest.calendar.HFCalendar.TICKS_PER_DAY;
import static joshie.harvest.calendar.HFCalendar.TWO_HOURS;

@HFEvents
public class NewDayHandler {
    //New day
    public static void newDay(final World world, CalendarDate today, CalendarDate yesterday) {
        DailyTickHandler tickables = HFTrackers.getTickables(world);
        tickables.processPhase(IDailyTickable.Phase.PRIORITY);
        tickables.processBlocks();
        tickables.processPhase(IDailyTickable.Phase.PRE);
        HFTrackers.<AnimalTrackerServer>getAnimalTracker(world).newDay();
        HFTrackers.<TownTrackerServer>getTownTracker(world).newDay(today, yesterday);
        tickables.processPhase(IDailyTickable.Phase.POST);
        tickables.processPhase(IDailyTickable.Phase.LAST);
        HFTrackers.markDirty(world);
    }

    //Server tick for new day
    @SubscribeEvent
    public void onTick(ServerTickEvent event) {
        if (!OfflineTickHandler.BLOCKED) {
            if (event.phase != Phase.END) return;
            World overworld = FMLCommonHandler.instance().getMinecraftServerInstance().worldServers[0];
            if (overworld.getWorldTime() % TICKS_PER_DAY == 1) {
                CalendarServer calendar = HFTrackers.<CalendarServer>getCalendar(overworld);
                CalendarDate yesterday = calendar.getDate().copy();
                HFTrackers.<CalendarServer>getCalendar(overworld).newDay(overworld);
                CalendarDate today = calendar.getDate().copy();
                for (PlayerTrackerServer player : HFTrackers.getPlayerTrackers()) {
                    player.newDay(yesterday, today);
                }

                AnimalTrackerServer.processQueue();
                DailyTickHandler.processQueue();
                for (World world : FMLCommonHandler.instance().getMinecraftServerInstance().worldServers) {
                    newDay(world, today, yesterday);
                }
            }

            //Bihourly Tick
            if (HFAnimals.OUTDOOR_HAPPINESS && overworld.getWorldTime() %TWO_HOURS == 0) {
                for (World world : FMLCommonHandler.instance().getMinecraftServerInstance().worldServers) {
                    HFTrackers.<AnimalTrackerServer>getAnimalTracker(world).biHourly();
                }
            }
        }
    }
}
