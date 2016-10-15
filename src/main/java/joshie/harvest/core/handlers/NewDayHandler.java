package joshie.harvest.core.handlers;

import joshie.harvest.animals.AnimalTrackerServer;
import joshie.harvest.api.ticking.IDailyTickable;
import joshie.harvest.calendar.CalendarServer;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.util.annotations.HFEvents;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.town.TownTrackerServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

import static joshie.harvest.calendar.HFCalendar.TICKS_PER_DAY;

@HFEvents
public class NewDayHandler {
    //New day
    public static void newDay(final World world) {
        HFTrackers.getTickables(world).newDay(IDailyTickable.Phase.PRE);
        HFTrackers.<AnimalTrackerServer>getAnimalTracker(world).newDay();
        HFTrackers.<TownTrackerServer>getTownTracker(world).newDay();
        HFTrackers.getTickables(world).newDay(IDailyTickable.Phase.POST);
        HFTrackers.markDirty(world);
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

                AnimalTrackerServer.processQueue();
                DailyTickHandler.processQueue();
                for (World world : FMLCommonHandler.instance().getMinecraftServerInstance().worldServers) {
                    newDay(world);
                }
            }
        }
    }
}
