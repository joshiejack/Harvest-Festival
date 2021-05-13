package uk.joshiejack.harvestcore.database;

import uk.joshiejack.harvestcore.registry.Note;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static uk.joshiejack.settlements.Settlements.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class NoteCategories {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) {
        event.table("note_categories").rows().forEach(row -> Note.ICONS.put(row.name(), row.icon()));
    }
}
