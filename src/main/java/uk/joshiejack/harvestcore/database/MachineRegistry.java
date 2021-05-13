package uk.joshiejack.harvestcore.database;

import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.tile.TileFurnace;
import uk.joshiejack.harvestcore.tile.TileKiln;
import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.helpers.DatabaseHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
public class MachineRegistry {
    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) {
        DatabaseHelper.registerSimpleMachine(event, "kiln", TileKiln.registry);
        event.table("furnace").rows().forEach(row -> {
            Holder holder = Holder.getFromString(row.get("input"));
            int count = row.get("count");
            long time = event.table("time_unit").fetch_where("name=" + row.get("duration")).getAsLong("duration");
            ItemStack output = StackHelper.getStackFromString(row.get("output"));
            if (!output.isEmpty()) {
                TileFurnace.registry.register(holder, new TileFurnace.Recipe(output, time, count));
            }
        });
    }
}
