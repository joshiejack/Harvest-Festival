package uk.joshiejack.penguinlib.ticker;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.data.adapters.StateAdapter;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = PenguinLib.MOD_ID)
public class TickerRegistry {
    private static final BiMap<String, DailyTicker> REGISTRY = HashBiMap.create();
    private static final Map<Block, DailyTicker> BLOCK_ENTRIES = new HashMap<>();
    private static final Map<IBlockState, DailyTicker> BLOCKSTATE_ENTRIES = new HashMap<>();

    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) {
        event.table("tickable_blocks").rows().forEach(row -> {
            String block = row.get("block");
            if (block.contains(" ")) {
                IBlockState state = StateAdapter.fromString(row.get("block"));
                TickerRegistry.registerBlockState(state, row.get("handler type"));
            } else {
                TickerRegistry.registerBlock(Block.REGISTRY.getObject(new ResourceLocation(block)), row.get("handler type"));
            }
        });
    }

    public static boolean isActive() {
        return REGISTRY.size() > 0 && (BLOCK_ENTRIES.size() > 0 || BLOCKSTATE_ENTRIES.size() > 0);
    }

    public static DailyTicker createEntryFromState(World world, Chunk chunk, BlockPos pos, IBlockState state) {
        DailyTicker entry = createTickingEntry(state);
        entry.onAdded(world, chunk, pos, state);
        return entry;
    }

    public static void registerType(String string, DailyTicker entry) {
        REGISTRY.put(string, entry);
    }

    public static void registerBlock(Block block, String type) {
        BLOCK_ENTRIES.put(block, REGISTRY.get(type));
        if (!(block instanceof BlockLeaves)) {
            block.setTickRandomly(false);
        }
    }

    public static void registerBlockState(IBlockState state, String type) {
        BLOCKSTATE_ENTRIES.put(state, REGISTRY.get(type));
        if (!(state.getBlock() instanceof BlockLeaves)) {
            state.getBlock().setTickRandomly(false);
        }
    }

    public static DailyTicker createTickingEntryFromString(String type) {
        DailyTicker entry = REGISTRY.get(type);
        return entry != null ? entry.newInstance() : null;
    }

    public static boolean isCorrectTypeForState(String type, IBlockState state) {
        return BLOCKSTATE_ENTRIES.get(state).getType().equals(type) || BLOCK_ENTRIES.get(state.getBlock()).getType().equals(type);
    }

    public static boolean hasTickingEntry(IBlockState state) {
        return BLOCKSTATE_ENTRIES.containsKey(state) || BLOCK_ENTRIES.containsKey(state.getBlock());
    }

    public static DailyTicker createTickingEntry(IBlockState state) {
        return BLOCKSTATE_ENTRIES.containsKey(state) ? BLOCKSTATE_ENTRIES.get(state).newInstance() : BLOCK_ENTRIES.get(state.getBlock()).newInstance();
    }
}
