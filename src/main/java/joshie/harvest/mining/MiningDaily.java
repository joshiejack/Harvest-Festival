package joshie.harvest.mining;

import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.core.util.annotations.HFEvents;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static joshie.harvest.mining.MiningTicker.MAX_LOOP;
import static net.minecraft.world.chunk.Chunk.NULL_BLOCK_STORAGE;

@HFEvents
public class MiningDaily {
    private void removeOresAndSpawnNew(World world, Chunk chunk) {
        ExtendedBlockStorage[] array = chunk.getBlockStorageArray();
        //Remove All previous ores from the chunk
        for (int loopY = 0; loopY < MAX_LOOP; loopY += MiningTicker.FLOOR_HEIGHT) {
            int y = loopY + 1;
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    ExtendedBlockStorage extendedblockstorage = array[y >> 4];
                    if (extendedblockstorage != NULL_BLOCK_STORAGE) {
                        if (extendedblockstorage.get(x, y & 15, z).getBlock() == HFMining.ORE) {
                            extendedblockstorage.set(x, y & 15, z, Blocks.AIR.getDefaultState());
                        }
                    }
                }
            }
        }

        //Add new ores
        for (int loopY = 0; loopY < MAX_LOOP; loopY += MiningTicker.FLOOR_HEIGHT) {
            int oreChance = world.rand.nextInt(5) == 0 ? 10 + world.rand.nextInt(15) : 25 + world.rand.nextInt(25);
            int floor = MiningHelper.getFloor(chunk.xPosition, loopY);
            int y = loopY + 1;
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    if (world.rand.nextInt(oreChance) == 0) {
                        ExtendedBlockStorage extendedblockstorage = array[y >> 4];
                        if (extendedblockstorage != NULL_BLOCK_STORAGE) {
                            IBlockState state = extendedblockstorage.get(x, y & 15, z);
                            if (state.getBlock() == Blocks.AIR) {
                                IBlockState set = MiningTicker.getBlockState(world.rand, floor);
                                if (set != null) {
                                    extendedblockstorage.set(x, y & 15, z, set);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkDataEvent.Load event) {
        if (event.getWorld().provider.getDimension() == HFMining.MINING_ID) {
            NBTTagCompound tag = getCompoundTag(event.getData());
            long chunk = ChunkPos.chunkXZ2Int(event.getChunk().xPosition, event.getChunk().zPosition);
            if (tag.hasKey("" + chunk)) {
                int days = CalendarHelper.getElapsedDays(event.getWorld().getWorldTime());
                int lastDay = tag.getInteger("" + chunk);
                if (days - lastDay > 0) {
                    removeOresAndSpawnNew(event.getWorld(), event.getChunk());
                }
            }
        }
    }

    @SubscribeEvent
    public void onChunkSave(ChunkDataEvent.Save event) {
        if (event.getWorld().provider.getDimension() == HFMining.MINING_ID) {
            NBTTagCompound tag = getCompoundTag(event.getData());
            long chunk = ChunkPos.chunkXZ2Int(event.getChunk().xPosition, event.getChunk().zPosition);
            int days = CalendarHelper.getElapsedDays(event.getWorld().getWorldTime());
            tag.setInteger("" + chunk, days);
        }
    }

    private NBTTagCompound getCompoundTag(NBTTagCompound tag) {
        if (tag.hasKey("LastTickData")) return tag.getCompoundTag("LastTickData");
        else {
            NBTTagCompound data = new NBTTagCompound();
            tag.setTag("LastTickData", data);
            return data;
        }
    }
}
