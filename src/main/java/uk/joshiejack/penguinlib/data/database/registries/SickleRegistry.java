package uk.joshiejack.penguinlib.data.database.registries;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.data.adapters.StateAdapter;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = PenguinLib.MOD_ID)
public class SickleRegistry {
    public static final Map<IBlockState, ItemStack> SICKLE = new HashMap<>();

    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) {
        event.table("sickle").rows()
                .forEach(row -> SICKLE.put(StateAdapter.fromString(row.get("state")), row.item()));
    }
}
