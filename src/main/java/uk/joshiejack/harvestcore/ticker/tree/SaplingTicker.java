package uk.joshiejack.harvestcore.ticker.tree;

import uk.joshiejack.penguinlib.ticker.DailyTicker;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SaplingTicker extends SeedlingTicker {
    private IBlockState target;

    public SaplingTicker(String type, IBlockState target, IBlockState next, int days) {
        super(type, next, days);
        this.target = target;
    }

    @Override
    public DailyTicker newInstance() {
        return new SaplingTicker(getType(), target, next, days);
    }

    @Override
    public void tick(World world, BlockPos pos, IBlockState state) {
        super.tick(world, pos, state);

        if (state != target) {
            world.setBlockState(pos, target);
        }
    }
}
