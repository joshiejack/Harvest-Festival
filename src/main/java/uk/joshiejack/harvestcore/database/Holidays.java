package uk.joshiejack.harvestcore.database;

import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.registry.Holiday;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.seasons.data.database.SeasonDataLoader;
import uk.joshiejack.seasons.handlers.SeasonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
public class Holidays {
    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) {
        event.table("holidays").rows().forEach(row -> {
            int day = row.get("day");
            SeasonHandler season = SeasonDataLoader.SEASON_HANDLERS.get(row.get("season").toString());
            Holiday.create(day, season, row.name());
        });
    }
}
