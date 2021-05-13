package uk.joshiejack.seasons.world;

import uk.joshiejack.penguinlib.events.NewDayEvent;
import uk.joshiejack.penguinlib.events.TimeChangedEvent;
import uk.joshiejack.seasons.Seasons;
import uk.joshiejack.seasons.SeasonsConfig;
import uk.joshiejack.seasons.world.storage.SeasonsSavedData;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Seasons.MODID)
public class SeasonsUpdater {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onWorldLoad(WorldEvent.Load event) {
        if (SeasonsConfig.dailyWeather && event.getWorld().provider.getDimension() == 0) {
            event.getWorld().provider.calculateInitialWeather();
        }
    }

    @SubscribeEvent
    public static void onPlayerJoinedWorld(PlayerEvent.PlayerLoggedInEvent event) {
        SeasonsSavedData.getWorldData(event.player.world).synchronize(event.player);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onNewDay(NewDayEvent event) {
        SeasonsSavedData.getWorldData(event.getWorld()).onNewDay(event.getWorld());
    }

    @SubscribeEvent
    public static void onTimeChanged(TimeChangedEvent event) {
        SeasonsSavedData.getWorldData(event.getWorld()).recalculate(event.getWorld(), true);
    }
}
