package uk.joshiejack.harvestcore.ticker.growable;

import uk.joshiejack.penguinlib.ticker.DailyTicker;
import uk.joshiejack.seasons.handlers.SeasonHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings("WeakerAccess")
public class GrowthTicker extends AbstractGrowableTicker {
    public GrowthTicker(String type, SeasonHandler seasons) {
        super(type, seasons);
    }

    @Override
    public DailyTicker newInstance() {
        return new GrowthTicker(getType(), seasons);
    }

    @Override
    public void tickInSeason(World world, BlockPos pos, IBlockState state) {
        world.immediateBlockTick(pos, state, world.rand);
    }
}
