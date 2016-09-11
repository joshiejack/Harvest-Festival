package joshie.harvest.crops;

import joshie.harvest.api.HFApi;
import joshie.harvest.core.util.HFEvents;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static net.minecraft.world.chunk.Chunk.NULL_BLOCK_STORAGE;

@HFEvents
public class FarmlandLoad {
    public static boolean register() { return HFCrops.VALIDATE_FARMLAND; }

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
                        if (extendedblockstorage.get(x, y & 15, z).getBlock() == Blocks.FARMLAND) {
                            BlockPos pos = new BlockPos((chunk.xPosition * 16) + x, y, (chunk.zPosition * 16) + z);
                            HFApi.tickable.addTickable(world, pos, HFApi.tickable.getTickableFromBlock(Blocks.FARMLAND));
                        }
                    }
                }
            }
        }
    }
}
