package uk.joshiejack.harvestcore.data;

import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.registry.Blueprint;
import uk.joshiejack.harvestcore.registry.Note;
import uk.joshiejack.harvestcore.registry.letter.Letter;
import uk.joshiejack.penguinlib.data.custom.CustomLoader;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
public class HarvestCoreCustomLoader {
    @SubscribeEvent
    public static void onLoadingCustomBuilder(CustomLoader.CustomBuilderLoading.Post event) {
        event.registerBuilder("blueprints", CustomLoader::build);
        event.registerBuilder("letters", CustomLoader::build);
        event.registerBuilder("notes", CustomLoader::build);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) {
        Blueprint.REGISTRY.entrySet().removeIf(b -> !b.getValue().init());
        Letter.REGISTRY.entrySet().removeIf(b -> !b.getValue().init());
        Note.REGISTRY.entrySet().removeIf(b -> !b.getValue().init());
    }
}
