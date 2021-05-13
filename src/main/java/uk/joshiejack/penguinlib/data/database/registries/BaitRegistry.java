package uk.joshiejack.penguinlib.data.database.registries;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.data.holder.HolderRegistry;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;

@Mod.EventBusSubscriber(modid = PenguinLib.MOD_ID)
public class BaitRegistry {
    public static final HolderRegistry<Integer> SPEED = new HolderRegistry<>(0);
    public static final HolderRegistry<Integer> LUCK = new HolderRegistry<>(0);

    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) {
        event.table("bait").rows().forEach(row -> {
            Holder holder = row.holder();
            SPEED.register(holder, row.getAsInt("speed"));
            LUCK.register(holder, row.getAsInt("luck"));
        });
    }
}
