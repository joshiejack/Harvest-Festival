package uk.joshiejack.harvestcore.ticker.growable;

import uk.joshiejack.penguinlib.ticker.DailyTicker;
import uk.joshiejack.seasons.handlers.SeasonHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractGrowableTicker extends DailyTicker {
    protected final SeasonHandler seasons;

    public AbstractGrowableTicker(String type, SeasonHandler seasons) {
        super(type);
        this.seasons = seasons;
    }

    @Override
    public final void tick(World world, BlockPos pos, IBlockState state) {
        if (seasons.isValidSeason(world, pos)) {
            tickInSeason(world, pos, state);
        }
    }

    protected abstract void tickInSeason(World world, BlockPos pos, IBlockState state);
}
