package uk.joshiejack.seasons.world.weather;

import uk.joshiejack.seasons.date.CalendarDate;
import uk.joshiejack.penguinlib.ticker.DailyTicker;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.WorldHelper;
import uk.joshiejack.seasons.Season;
import uk.joshiejack.seasons.date.DateHelper;
import uk.joshiejack.seasons.world.storage.SeasonsSavedData;
import uk.joshiejack.seasons.world.storage.WorldDataServer;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings("WeakerAccess")
@PenguinLoader("snow")
public class SnowTicker extends DailyTicker {
    public SnowTicker() {
        super("snow");
    }

    @Override
    public DailyTicker newInstance() {
        return new SnowTicker();
    }

    @Override
    public void tick(World world, BlockPos pos, IBlockState state) {
        if (world.getBlockState(pos).getBlock() == Blocks.SNOW_LAYER) {
            boolean canSnow = WorldHelper.snows(world, pos);
            WorldDataServer data = SeasonsSavedData.getWorldData(world);
            Weather weather = data.getWeather();
            if (canSnow && weather != Weather.CLEAR) {
                if (weather == Weather.STORM) {
                    int meta = state.getValue(BlockSnow.LAYERS);
                    if (meta < 6) {
                        world.setBlockState(pos, state.withProperty(BlockSnow.LAYERS, meta + 1), 2);
                    }
                }

                for (BlockPos p: BlockPos.getAllInBox(pos.north(2).east(2), pos.south(2).west(2))) {
                    if (world.rand.nextInt(3) > 0) continue;
                    BlockPos top = world.getTopSolidOrLiquidBlock(p);
                    if (world.isAirBlock(top) && world.canBlockSeeSky(top) && Blocks.SNOW_LAYER.canPlaceBlockAt(world, top)) {
                        world.setBlockState(top, Blocks.SNOW_LAYER.getDefaultState(), 2);
                    }

                    //BlockPos down = top.down();
                    if (world.getBlockState(top).getBlock() instanceof BlockBush && world.canBlockSeeSky(top)) {
                        world.setBlockState(top, Blocks.SNOW_LAYER.getDefaultState(), 2);
                        world.notifyNeighborsOfStateChange(top, Blocks.AIR, false);
                    }
                }
            } else if (!canSnow) {
                Season season = data.getSeason();
                CalendarDate date = DateHelper.getDate(world);
                int chance = Math.max(1, (6 - date.getDay() - weather.ordinal()));
                if (season == Season.AUTUMN || season == Season.SUMMER || (season == Season.SPRING && world.rand.nextInt(chance) == 0)) {
                    int meta = state.getValue(BlockSnow.LAYERS);
                    if (meta > 1) world.setBlockState(pos, state.withProperty(BlockSnow.LAYERS, Math.max(1, meta - (world.rand.nextInt(meta + 1)))), 2);
                    else {
                        if (world.rand.nextInt(64) == 0 && date.getDay() < 8) {
                            IBlockState down = world.getBlockState(pos.down());
                            if (down.getBlock() instanceof IGrowable) {
                                IGrowable growable = ((IGrowable) down.getBlock());
                                if (growable.canGrow(world, pos.down(), down, false)) {
                                    if (growable.canUseBonemeal(world, world.rand, pos.down(), down)) {
                                        growable.grow(world, world.rand, pos.down(), down);
                                    }
                                }
                            }
                        } else world.setBlockToAir(pos);
                    }
                }
            }
        }
    }
}
