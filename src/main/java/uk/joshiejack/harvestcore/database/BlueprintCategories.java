package uk.joshiejack.harvestcore.database;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.harvestcore.registry.Blueprint;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;

import static uk.joshiejack.settlements.Settlements.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class BlueprintCategories {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) {
        event.table("blueprint_categories").rows().forEach(row -> Blueprint.ICONS.put(row.name(), row.icon()));
    }
}
