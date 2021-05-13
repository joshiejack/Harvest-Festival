package uk.joshiejack.penguinlib.ticker.data;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.commons.lang3.tuple.Pair;
import uk.joshiejack.penguinlib.ticker.DailyTicker;
import uk.joshiejack.penguinlib.ticker.TickerRegistry;

import java.util.*;

public class ChunkData {
    private final Long2ObjectMap<DailyTicker> entries = new Long2ObjectOpenHashMap<>();
    private final Set<Long> toRemove = new HashSet<>();

    boolean hasEntry(BlockPos pos) {
        return entries.containsKey(pos.toLong());
    }

    public boolean isInvalid(IBlockState state, BlockPos pos) {
        return !TickerRegistry.isCorrectTypeForState(entries.get(pos.toLong()).getType(), state);
    }

    void addEntry(World world, BlockPos pos, Chunk chunk, IBlockState state) {
        entries.put(pos.toLong(), TickerRegistry.createEntryFromState(world, chunk, pos, state));
        toRemove.remove(pos.toLong()); //Don't remove this as we've just created a new entry
    }

    void removeEntry(BlockPos pos) {
        toRemove.add(pos.toLong());
    }

    List<Pair<BlockPos, DailyTicker>> getSorted() {
        List<Pair<BlockPos, DailyTicker>> list = Lists.newArrayList();
        entries.forEach((k, v) -> list.add(Pair.of(BlockPos.fromLong(k), v)));
        list.sort(Comparator.comparingInt(pair -> pair.getValue().getPriority().ordinal()));
        return list;
    }

    public boolean remove() {
        toRemove.forEach(entries::remove);
        toRemove.clear();
        return true;
    }

    public Long2ObjectMap<DailyTicker> entries() {
        return entries;
    }

    DailyTicker getEntry(BlockPos pos) {
        return entries.get(pos.toLong());
    }

    void writeToNBT(NBTTagList list) {
        remove(); //Clear everything up here
        entries.forEach((p, t) -> {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setLong("Pos", p);
                tag.setString("Type", t.getType());
                if (t instanceof INBTSerializable) {
                    tag.setTag("Data", ((INBTSerializable)t).serializeNBT());
                }

                list.appendTag(tag);
            }
        );
    }

    public static final List<Pair<BlockPos, DailyTicker>> EMPTY = new ArrayList<>();

    @SuppressWarnings("all")
    List<Pair<BlockPos, DailyTicker>> readFromNBT(World world, NBTTagList nbt) {
        List<Pair<BlockPos, DailyTicker>> dailyTickers = EMPTY;
        for (int i = 0; i < nbt.tagCount(); i++) {
            NBTTagCompound tag = nbt.getCompoundTagAt(i);
            BlockPos position = BlockPos.fromLong(tag.getLong("Pos"));
            String savedTypeString = tag.getString("Type");
            DailyTicker tickerType = TickerRegistry.createTickingEntryFromString(savedTypeString);
            if (tickerType instanceof INBTSerializable) {
                ((INBTSerializable)tickerType).deserializeNBT(tag.getCompoundTag("Data"));
            }

            if (tickerType.tickOnLoad()) {
                if (tickerType.tickOnLoad()) {
                    if (dailyTickers.isEmpty()) {
                        dailyTickers = new ArrayList<>();
                    }

                    dailyTickers.add(Pair.of(position, tickerType));
                }
            }
        }

        return dailyTickers;
    }
}
