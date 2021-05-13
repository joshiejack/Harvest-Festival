package uk.joshiejack.penguinlib.ticker;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.eventhandler.EventPriority;

public abstract class DailyTicker {
    private final String type;
    private boolean ticksOnLoad;

    public DailyTicker(String type) {
        this.type = type;
    }

    public EventPriority getPriority() {
        return EventPriority.NORMAL;
    }

    public String getType() {
        return type;
    }

    public abstract DailyTicker newInstance();

    public void setTicksOnLoad() {
        this.ticksOnLoad = true;
    }

    public void onAdded(World world, Chunk chunk, BlockPos pos, IBlockState state) {}
    public void onChanged(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {}
    public void tick(World world, BlockPos pos, IBlockState state) {}

    public boolean tickOnLoad() {
        return ticksOnLoad;
    }
}
