package uk.joshiejack.settlements.data.custom;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.settlements.building.Building;
import uk.joshiejack.penguinlib.data.custom.CustomLoader;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;

@Mod.EventBusSubscriber(modid = Settlements.MODID)
public class AdventureCustomLoader {
    @SubscribeEvent
    public static void onLoadingCustomBuilder(CustomLoader.CustomBuilderLoading.Pre event) {
        event.registerBuilder("buildings", CustomLoader::build);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) {
        Building.REGISTRY.entrySet().removeIf(b -> !b.getValue().init());
    }
}
