package uk.joshiejack.penguinlib.data.database.registries;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.data.adapters.StateAdapter;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(modid = PenguinLib.MOD_ID)
public class SmashRegistry {
    public static final Set<Block> ALLOWED_BLOCKS = new HashSet<>();
    public static final Set<IBlockState> ALLOWED_STATES = new HashSet<>();

    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) {
        event.table("smashable").rows().forEach(row -> {
            String block = row.get("block");
            if (block.contains("[") || block.contains(" ")) {
                IBlockState state = StateAdapter.fromString(block);
                if (state != null) ALLOWED_STATES.add(state);
            } else {
                Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block));
                if (b != null) ALLOWED_BLOCKS.add(b);
            }
        });
    }
}
