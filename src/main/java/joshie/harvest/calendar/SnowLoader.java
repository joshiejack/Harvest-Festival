package joshie.harvest.calendar;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weather;
import joshie.harvest.api.ticking.IDailyTickableBlock;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.util.annotations.HFEvents;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static net.minecraft.block.BlockSnow.LAYERS;
import static net.minecraft.world.chunk.Chunk.NULL_BLOCK_STORAGE;

@HFEvents
public class SnowLoader {
    public static IDailyTickableBlock SNOW_LAYER;

    public static boolean register() {
        if (HFCalendar.SNOW_TICKER) {
            SNOW_LAYER = (world, pos, state) -> {
                Calendar calendar = HFTrackers.getCalendar(world);
                Season season = calendar.getDate().getSeason();
                Weather weather = calendar.getTodaysWeather();
                if (!weather.isSnow()) {
                    if (season == Season.WINTER && !weather.isRain()) {
                        if (weather == Weather.BLIZZARD) {
                            if(world.getBlockState(pos).getBlock() == Blocks.SNOW_LAYER) {
                                int meta = state.getValue(LAYERS);
                                if (meta < 3) {
                                    world.setBlockState(pos, state.withProperty(LAYERS, meta + 1), 2);
                                }
                            } else return false;
                        }
                    } else if (world.rand.nextInt(3) < 2) {
                        return world.getBlockState(pos).getBlock() == Blocks.SNOW_LAYER && !world.setBlockToAir(pos);
                    }
                }

                return true;
            };
            return true;
        }

        return false;
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
                        if (extendedblockstorage.get(x, y & 15, z).getBlock() == Blocks.SNOW_LAYER) {
                            BlockPos pos = new BlockPos((chunk.xPosition * 16) + x, y, (chunk.zPosition * 16) + z);
                            if (!chunk.getBiome(pos, world.provider.getBiomeProvider()).isSnowyBiome()) {
                                HFApi.tickable.addTickable(world, pos, SNOW_LAYER);
                            }
                        }
                    }
                }
            }
        }
    }
}
