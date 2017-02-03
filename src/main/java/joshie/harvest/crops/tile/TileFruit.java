package joshie.harvest.crops.tile;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.ticking.DailyTickableBlock;
import joshie.harvest.api.ticking.DailyTickableBlock.Phases;
import joshie.harvest.core.base.tile.TileHarvest;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.crops.block.BlockFruit.Fruit;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileFruit extends TileHarvest {
    private static final DailyTickableBlock TICKABLE = new DailyTickableBlock(Phases.POST) {
        @Override
        public boolean isStateCorrect(World world, BlockPos pos, IBlockState state) {
            return state.getBlock() == HFCrops.FRUITS;
        }

        @Override
        public void newDay(World world, BlockPos pos, IBlockState state) {
            Fruit fruit = HFCrops.FRUITS.getEnumFromState(world.getBlockState(pos));
            Season season = fruit.getCrop().getSeasons()[0];
            if (HFApi.calendar.getDate(world).getSeason() != season) {
                world.setBlockToAir(pos);
            }
        }
    };

    @Override
    public DailyTickableBlock getTickableForTile() {
        return TICKABLE;
    }
}
