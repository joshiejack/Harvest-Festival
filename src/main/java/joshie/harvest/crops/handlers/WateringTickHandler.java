package joshie.harvest.crops.handlers;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.crops.WateringHandler;
import joshie.harvest.api.ticking.IDailyTickableBlock;
import joshie.harvest.core.util.annotations.HFEvents;
import joshie.harvest.crops.CropHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static net.minecraft.world.chunk.Chunk.NULL_BLOCK_STORAGE;

public class WateringTickHandler {
    @HFEvents
    public static class Load {
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
                            IBlockState state = extendedblockstorage.get(x, y & 15, z);
                            BlockPos pos = new BlockPos((chunk.xPosition * 16) + x, y, (chunk.zPosition * 16) + z);
                            WateringHandler handler = CropHelper.getWateringHandler(world, pos, state);
                            if (handler != null) {
                                HFApi.tickable.addTickable(world, pos, Ticker.INSTANCE);
                                if (!handler.isWet(world, pos, state) && world.isRaining()) {
                                    handler.hydrate(world, pos, state);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static class Ticker implements IDailyTickableBlock {
        public static final Ticker INSTANCE = new Ticker();
        @Override
        public boolean newDay(World world, BlockPos pos, IBlockState state) {
            WateringHandler handler = CropHelper.getWateringHandler(world, pos, state);
            if (handler != null) {
                if (CropHelper.isRainingAt(world, pos.up(2))) {
                    if (!handler.isWet(world, pos, state)) handler.hydrate(world, pos, state);
                } else handler.dehydrate(world, pos, state);

                return true;
            } else return false;
        }
    }
}
