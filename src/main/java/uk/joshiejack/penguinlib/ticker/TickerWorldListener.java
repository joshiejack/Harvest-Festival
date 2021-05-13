package uk.joshiejack.penguinlib.ticker;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.event.world.BlockEvent.CropGrowEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.tuple.Pair;
import uk.joshiejack.penguinlib.events.NewDayEvent;
import uk.joshiejack.penguinlib.ticker.data.ChunkData;
import uk.joshiejack.penguinlib.ticker.data.TickerData;
import uk.joshiejack.penguinlib.ticker.data.WorldData;
import uk.joshiejack.penguinlib.util.world.EmptyWorldEventListener;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static net.minecraft.world.chunk.Chunk.NULL_BLOCK_STORAGE;

@SuppressWarnings("unused")
public class TickerWorldListener extends EmptyWorldEventListener {
    static TickerWorldListener INSTANCE = null;
    private final TickerData data = new TickerData();

    public ChunkData getDataForChunk(int dimension, long chunk) {
        return data.get(dimension).get(chunk);
    }

    public static TickerWorldListener create() {
        INSTANCE = new TickerWorldListener();
        return INSTANCE;
    }

    @SubscribeEvent
    public void onGrowthTick(CropGrowEvent.Pre event) {
        if (TickerRegistry.hasTickingEntry(event.getState())) {
            event.setResult(Event.Result.ALLOW);
        }
    }

    @Nullable
    public DailyTicker getTicker(World world, BlockPos pos) {
        return data.getTicker(world, pos);
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (!event.getWorld().isRemote) {
            event.getWorld().addEventListener(this);
            data.load(event.getWorld().provider.getDimension());
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (!event.getWorld().isRemote) {
            event.getWorld().removeEventListener(this);
            data.unload(event.getWorld().provider.getDimension());
        }
    }

    @SubscribeEvent //Add all the existing blocks to the mappings
    public void onChunkData(ChunkDataEvent.Save event) {
        if (!event.getWorld().isRemote) {
            NBTTagCompound tag = event.getData();
            NBTTagList tagList = new NBTTagList();
            data.get(event.getWorld().provider.getDimension()).writeToNBT(event.getChunk(), tagList);
            tag.setTag("HFTickData", tagList);
        }
    }

    @SubscribeEvent //Load all our saved blocks
    public void onChunkData(ChunkDataEvent.Load event) {
        if (event.getWorld() instanceof WorldServer) {
            NBTTagCompound tag = event.getData();
            NBTTagList tagList = tag.getTagList("HFTickData", 10);
            WorldData world = data.load(event.getWorld().provider.getDimension());
            List<Pair<BlockPos, DailyTicker>> reloadEntries = world.readFromNBT(event.getChunk(), tagList);
            MinecraftServer server = event.getWorld().getMinecraftServer();
            if (server != null) {
                server.addScheduledTask(() -> reloadEntries.forEach((pair) -> pair.getRight()
                        .tick(event.getWorld(), pair.getLeft(), event.getWorld().getBlockState(pair.getLeft()))));
            }
        }
    }

    @SubscribeEvent //Wait for the chunk to actually load then check it
    public void onChunkLoad(ChunkEvent.Load event) {
        if (event.getWorld().isRemote) return;
        ExtendedBlockStorage[] array = event.getChunk().getBlockStorageArray();
        Chunk chunk = event.getChunk();
        World world = chunk.getWorld();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 256; y++) {
                    ExtendedBlockStorage extendedblockstorage = array[y >> 4];
                    if (extendedblockstorage != NULL_BLOCK_STORAGE) {
                        IBlockState state = extendedblockstorage.get(x, y & 15, z);
                        BlockPos pos = new BlockPos((chunk.x * 16) + x, y, (chunk.z * 16) + z);
                        if (state.getBlock() != Blocks.AIR && TickerRegistry.hasTickingEntry(state)) {
                            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() ->
                                    data.get(world.provider.getDimension()).addEntryIfNotExists(world, chunk, pos, state));
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onNewDay(NewDayEvent event) {
        MinecraftServer server = event.getWorld().getMinecraftServer();
        if (server != null) {
            server.addScheduledTask(() ->
                    data.get(event.getWorld().provider.getDimension())
                            .process(event.getWorld())); //Tell minecraft to update the world
        }
    }

    @Override
    public void notifyBlockUpdate(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState oldState, @Nonnull IBlockState newState, int flags) {
        MinecraftServer server = world.getMinecraftServer();
        if (server != null) {
            if (!TickerRegistry.hasTickingEntry(newState) && TickerRegistry.hasTickingEntry(oldState))
                server.addScheduledTask(() ->
                        data.get(world.provider.getDimension()).removeEntry(world.getChunk(pos), pos));
            else if (oldState.getBlock() != newState.getBlock() && TickerRegistry.hasTickingEntry(newState)) {
                server.addScheduledTask(() ->
                        data.get(world.provider.getDimension()).addEntry(world, world.getChunk(pos), pos, newState));
            } else if (oldState.getBlock() == newState.getBlock() && TickerRegistry.hasTickingEntry(newState)) {
                server.addScheduledTask(() ->
                        data.get(world.provider.getDimension()).onChanged(world, pos, oldState, newState));
            }
        }
    }
}
