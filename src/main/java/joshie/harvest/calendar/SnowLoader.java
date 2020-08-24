package joshie.harvest.calendar;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weather;
import joshie.harvest.api.ticking.DailyTickableBlock;
import joshie.harvest.calendar.data.Calendar;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.util.annotations.HFEvents;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static net.minecraft.block.BlockSnow.LAYERS;
import static net.minecraft.init.Blocks.SNOW_LAYER;
import static net.minecraft.world.chunk.Chunk.NULL_BLOCK_STORAGE;

@HFEvents
public class SnowLoader extends DailyTickableBlock {
    public static final SnowLoader INSTANCE = new SnowLoader();

    public SnowLoader() {
        super(Phases.PRE);
    }

    @Override
    public boolean isStateCorrect(World world, BlockPos pos, IBlockState state) {
        return state.getBlock() == Blocks.SNOW_LAYER || state.getBlock() == Blocks.ICE;
    }

    @Override
    public void newDay(World world, BlockPos pos, IBlockState state) {
        Calendar calendar = HFTrackers.getCalendar(world);
        Weather weather = calendar.getTodaysWeather();
        if (!weather.isSnow()) {
            if (calendar.getDate().getSeason() != Season.WINTER && world.rand.nextInt(3) < 2) {
                if (state.getBlock() == Blocks.SNOW_LAYER) {
                    world.setBlockToAir(pos); //Destroy the snow layer
                    //Attempt to grow some plants in spring, as we will have destroyed them in the winter
                    if (world.rand.nextInt(32) == 0) {
                        IBlockState down = world.getBlockState(pos.down());
                        if (down.getBlock() instanceof IGrowable) {
                            IGrowable growable = ((IGrowable)down.getBlock());
                            if (growable.canGrow(world, pos.down(), down, false)) {
                                if (growable.canUseBonemeal(world, world.rand, pos.down(), down)) {
                                    growable.grow(world, world.rand, pos.down(), down);
                                }
                            }
                        }
                    }
                } else world.setBlockState(pos, Blocks.WATER.getDefaultState());
            }
        } else if (weather == Weather.BLIZZARD && state.getBlock() == Blocks.SNOW_LAYER) {
            int meta = state.getValue(LAYERS);
            if (meta < 5) {
                world.setBlockState(pos, state.withProperty(LAYERS, meta + 1), 2);
            }
        }
    }

    @SubscribeEvent
    public void onChunkData(ChunkDataEvent.Load event) {
        ExtendedBlockStorage[] array = event.getChunk().getBlockStorageArray();
        Chunk chunk = event.getChunk();
        World world = chunk.getWorld();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 256; y++) {
                    ExtendedBlockStorage extendedblockstorage = array[y >> 4];
                    if (extendedblockstorage != NULL_BLOCK_STORAGE) {
                        if (extendedblockstorage.get(x, y & 15, z).getBlock() == SNOW_LAYER || extendedblockstorage.get(x, y & 15, z).getBlock() == Blocks.ICE) {
                            BlockPos pos = new BlockPos((chunk.x * 16) + x, y, (chunk.z * 16) + z);
                            if (!chunk.getBiome(pos, world.provider.getBiomeProvider()).isSnowyBiome()) {
                                HFApi.tickable.addTickable(world, pos, this);
                            }
                        }
                    }
                }
            }
        }
    }
}
