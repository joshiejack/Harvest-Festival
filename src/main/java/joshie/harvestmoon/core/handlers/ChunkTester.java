package joshie.harvestmoon.core.handlers;

import joshie.harvestmoon.HarvestMoon;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import org.apache.logging.log4j.Level;

public class ChunkTester {
    public static void activate(World world, int xCoord, int yCoord, int zCoord) {
        // if (!world.isRemote) {
        long current = System.currentTimeMillis();
        HarvestMoon.logger.log(Level.INFO, "Starting: " + current);
        Chunk chunk = world.getChunkFromChunkCoords(xCoord >> 4, zCoord >> 4);
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 256; y++) {
                for (int z = 0; z < 16; z++) {
                    Block block = chunk.getBlock(x, y, z);
                    chunk.func_150807_a(x, y, z, Blocks.cobblestone, 1);
                }
            }
        }

        long complete = System.currentTimeMillis();
        HarvestMoon.logger.log(Level.INFO, "Ending: " + System.currentTimeMillis());
        HarvestMoon.logger.log(Level.INFO, world.isRemote + "Total MS: " + (complete - current));
        // }
    }
}
