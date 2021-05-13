package uk.joshiejack.penguinlib.ticker;

import uk.joshiejack.penguinlib.ticker.data.ChunkData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TickerHelper {
    @Nullable
    public static DailyTicker getTicker(World world, BlockPos pos) {
        return TickerWorldListener.INSTANCE != null ? TickerWorldListener.INSTANCE.getTicker(world, pos) : null;
    }

    @Nullable
    public static ChunkData getChunkData(int dimension, long chunk) {
        return TickerWorldListener.INSTANCE != null ? TickerWorldListener.INSTANCE.getDataForChunk(dimension, chunk) : null;
    }
}
