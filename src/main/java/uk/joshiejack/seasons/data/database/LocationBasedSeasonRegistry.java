package uk.joshiejack.seasons.data.database;

import com.google.common.collect.Lists;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.scripting.Interpreter;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.seasons.Seasons;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid = Seasons.MODID)
public class LocationBasedSeasonRegistry {
    private static final List<Interpreter> handlers = Lists.newArrayList();

    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) {
        event.table("season_location_handlers").rows()
                .stream().filter(row -> Scripting.scriptExists(row.getScript()))
                .forEach(row -> handlers.add(Scripting.getScript(row.getScript())));
    }

    public static List<Interpreter> getSeasonHandlers() {
        return handlers;
    }
}

