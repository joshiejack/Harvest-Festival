package uk.joshiejack.penguinlib.ticker.data;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import uk.joshiejack.penguinlib.ticker.DailyTicker;

import javax.annotation.Nullable;

public class TickerData {
    private final Int2ObjectMap<WorldData> data = new Int2ObjectOpenHashMap<>();

    @Nullable
    public DailyTicker getTicker(World world, BlockPos pos) {
        return data.containsKey(world.provider.getDimension()) ? data.get(world.provider.getDimension()).getEntryAt(world.getChunk(pos), pos) : null;
    }

    public WorldData get(int dimension) {
        return data.get(dimension);
    }

    public WorldData load(int dimension) {
        if (!data.containsKey(dimension)) {
            data.put(dimension, new WorldData());
        }

        return data.get(dimension);
    }

    public void unload(int dimension) {
        data.remove(dimension);
    }

    public NBTTagList serializeNBT() {
        return null;
    }

    public void deserializeNBT(NBTTagList nbt) {

    }
}
