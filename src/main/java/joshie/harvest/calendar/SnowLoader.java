package joshie.harvest.calendar;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weather;
import joshie.harvest.api.ticking.DailyTickableBlock;
import joshie.harvest.calendar.data.Calendar;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.util.annotations.HFEvents;
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

    @Override
    public boolean isStateCorrect(World world, BlockPos pos, IBlockState state) {
        return state.getBlock() == Blocks.SNOW_LAYER;
    }

    @Override
    public void newDay(World world, BlockPos pos, IBlockState state) {
        Calendar calendar = HFTrackers.getCalendar(world);
        Weather weather = calendar.getTodaysWeather();
        if (!weather.isSnow()) {
            if (calendar.getDate().getSeason() != Season.WINTER && world.rand.nextInt(3) < 2) {
                world.setBlockToAir(pos);
            }
        } else if (weather == Weather.BLIZZARD) {
            int meta = state.getValue(LAYERS);
            if (meta < 3) {
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
                        if (extendedblockstorage.get(x, y & 15, z).getBlock() == SNOW_LAYER) {
                            BlockPos pos = new BlockPos((chunk.xPosition * 16) + x, y, (chunk.zPosition * 16) + z);
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
