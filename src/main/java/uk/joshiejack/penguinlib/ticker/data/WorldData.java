package uk.joshiejack.penguinlib.ticker.data;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import org.apache.commons.lang3.tuple.Pair;
import uk.joshiejack.penguinlib.ticker.DailyTicker;

import javax.annotation.Nullable;
import java.util.List;

public class WorldData {
    private final Long2ObjectMap<ChunkData> entries = new Long2ObjectOpenHashMap<>();

    @Nullable
    public ChunkData get(long chunk) {
        return entries.get(chunk);
    }

    private ChunkData getOrCreateEntry(Chunk chunk) {
        long key = ChunkPos.asLong(chunk.x, chunk.z);
        entries.putIfAbsent(key, new ChunkData());
        return entries.get(key);
    }

    public void addEntryIfNotExists(World world, Chunk chunk, BlockPos pos, IBlockState state) {
        ChunkData entries = getOrCreateEntry(chunk);
        if (!entries.hasEntry(pos) || entries.isInvalid(state, pos)) {
            entries.addEntry(world, pos, chunk, state);
        }
    }

    public void addEntry(World world, Chunk chunk, BlockPos pos, IBlockState state) {
        getOrCreateEntry(chunk).addEntry(world, pos, chunk, state);
    }

    public void onChanged(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        DailyTicker entry = getEntryAt(world.getChunk(pos), pos);
        if (entry != null) entry.onChanged(world, pos, oldState, newState);
    }


    public void removeEntry(Chunk chunk, BlockPos pos) {
        getOrCreateEntry(chunk).removeEntry(pos);
    }

    @Nullable
    public DailyTicker getEntryAt(Chunk chunk, BlockPos pos) {
        return getOrCreateEntry(chunk).getEntry(pos);
    }

    public void process(WorldServer world) {
        world.getChunkProvider().loadedChunks.keySet().stream()
                .filter(key -> entries.containsKey(key) && entries.get(key).remove())
                .map(id -> entries.get(id).getSorted())
                .flatMap(List::stream)
                .forEach(pair -> {
                    BlockPos pos = pair.getKey();
                    pair.getValue().tick(world, pos, world.getBlockState(pos));
                });
    }

    public void writeToNBT(Chunk chunk, NBTTagList data) {
        long pos = ChunkPos.asLong(chunk.x, chunk.z);
        if (entries.containsKey(pos)) {
            entries.get(pos).writeToNBT(data);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Pair<BlockPos, DailyTicker>> readFromNBT(Chunk chunk, NBTTagList data) {
        if (data.tagCount() > 0) {
            return getOrCreateEntry(chunk).readFromNBT(chunk.getWorld(), data);
        } else return ChunkData.EMPTY;
    }
}
