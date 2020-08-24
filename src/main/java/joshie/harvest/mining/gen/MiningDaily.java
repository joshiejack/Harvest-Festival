package joshie.harvest.mining.gen;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.util.annotations.HFEvents;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.MiningHelper;
import joshie.harvest.mining.MiningRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static joshie.harvest.mining.MiningHelper.MAX_LOOP;
import static net.minecraft.world.chunk.Chunk.NULL_BLOCK_STORAGE;

@HFEvents
@SuppressWarnings("unused")
public class MiningDaily {
    private void removeOresAndSpawnNew(World world, Chunk chunk) {
        ExtendedBlockStorage[] array = chunk.getBlockStorageArray();
        //Remove All previous ores from the chunk
        for (int loopY = 0; loopY < MAX_LOOP; loopY += MiningHelper.FLOOR_HEIGHT) {
            int y = loopY + 1;
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    ExtendedBlockStorage extendedblockstorage = array[y >> 4];
                    if (extendedblockstorage != NULL_BLOCK_STORAGE) {
                        if (MiningRegistry.INSTANCE.all.contains(extendedblockstorage.get(x, y & 15, z))) {
                            extendedblockstorage.set(x, y & 15, z, Blocks.AIR.getDefaultState());
                        }
                    }
                }
            }
        }

        //Add new ores
        Season season = HFApi.calendar.getDate(world).getSeason();
        for (int loopY = 0; loopY < MAX_LOOP; loopY += MiningHelper.FLOOR_HEIGHT) {
            int floor = MiningHelper.getFloor(chunk.x, loopY);
            int oreChance = MiningHelper.getOreChance(season, floor, world.rand);
            int y = loopY + 1;
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    if (world.rand.nextInt(oreChance) == 0) {
                        ExtendedBlockStorage extendedblockstorage = array[y >> 4];
                        if (extendedblockstorage != NULL_BLOCK_STORAGE) {
                            IBlockState state = extendedblockstorage.get(x, y & 15, z);
                            if (state.getBlock() == Blocks.AIR) {
                                IBlockState set = MiningRegistry.INSTANCE.getRandomStateForSeason(world, floor, season);
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
            NBTTagCompound tag = NBTHelper.getLastTickData(event.getData());
            long chunk = ChunkPos.asLong(event.getChunk().x, event.getChunk().z);
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
            NBTTagCompound tag = NBTHelper.getLastTickData(event.getData());
            long chunk = ChunkPos.asLong(event.getChunk().x, event.getChunk().z);
            int days = CalendarHelper.getElapsedDays(event.getWorld().getWorldTime());
            tag.setInteger("" + chunk, days);
        }
    }
}
