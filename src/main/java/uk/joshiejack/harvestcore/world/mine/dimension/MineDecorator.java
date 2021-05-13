package uk.joshiejack.harvestcore.world.mine.dimension;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.world.mine.Mine;
import uk.joshiejack.harvestcore.world.mine.MineHelper;
import uk.joshiejack.harvestcore.world.mine.dimension.decorators.DecoratorLoot;
import uk.joshiejack.harvestcore.world.mine.dimension.wrappers.DecoratorWrapperExtendedBlockStorage;
import uk.joshiejack.harvestcore.world.mine.tier.Tier;
import uk.joshiejack.penguinlib.util.BlockStates;
import uk.joshiejack.penguinlib.util.helpers.minecraft.NBTHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.TimeHelper;

import java.util.Set;

import static net.minecraft.world.chunk.Chunk.NULL_BLOCK_STORAGE;
import static uk.joshiejack.harvestcore.world.mine.dimension.MineChunkGenerator.CHUNKS_PER_SECTION;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
public class MineDecorator {
    public static final Set<Block> GENERATED = Sets.newHashSet();

    private static void removeDecorations(World world, Chunk chunk) {
        ExtendedBlockStorage[] array = chunk.getBlockStorageArray();
        //Remove All previous ores from the chunk
        for (int y = 1; y < 241; y += 6) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    ExtendedBlockStorage extendedblockstorage = array[y >> 4];
                    if (extendedblockstorage != NULL_BLOCK_STORAGE) {
                        IBlockState state = extendedblockstorage.get(x, y & 15, z);
                        if (GENERATED.contains(extendedblockstorage.get(x, y & 15, z).getBlock())) {
                            extendedblockstorage.set(x, y & 15, z, Blocks.AIR.getDefaultState());
                        }
                    }
                }
            }
        }
    }

    public static double getDecorateChance(int actualFloor) {
        return Math.max(0.021, Math.min(0.1, ((double) actualFloor - 1) / 12D));
    }

    public static void spawnDecorations(World world, Chunk chunk) {
        int tierNumber = chunk.z / CHUNKS_PER_SECTION; //The Tier of this section of the mine
        Tier tier = Mine.BY_ID.get(world.provider.getDimension()).getTierFromInt(tierNumber);
        ExtendedBlockStorage[] array = chunk.getBlockStorageArray();
        for (int y = 2; y < 242; y+= 6) {
            ExtendedBlockStorage extendedblockstorage = array[y >> 4];
            int floor = MineHelper.getRelativeFloor(y);
            int actualFloor = MineHelper.getFloorFromCoordinates(chunk.z, floor);
            DecoratorWrapperExtendedBlockStorage wrapper = new DecoratorWrapperExtendedBlockStorage(extendedblockstorage, tier, world.rand, floor);
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (world.rand.nextDouble() <= getDecorateChance(actualFloor)) {
                        if (extendedblockstorage != NULL_BLOCK_STORAGE) {
                            IBlockState state = extendedblockstorage.get(x, y & 15, z);
                            if (state == BlockStates.AIR && wrapper.getBlockState(pos.down()).isFullCube()) {
                                DecoratorLoot loot = tier.getLoot(wrapper.rand, wrapper.floor, actualFloor);
                                if (loot != null && loot.isValid(wrapper, pos)) {
                                    loot.decorate(wrapper, pos);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkDataEvent.Load event) {
        if (Mine.BY_ID.containsKey(event.getWorld().provider.getDimension())) {
            if ((event.getChunk().x < 0 || event.getChunk().z < 0)) return;
            NBTTagCompound tag = NBTHelper.getOrCreateTag(event.getData(), "LastUpdate");
            long chunk = ChunkPos.asLong(event.getChunk().x, event.getChunk().z);
            if (tag.hasKey("" + chunk)) {
                int days = TimeHelper.getElapsedDays(event.getWorld().getWorldTime());
                int lastDay = tag.getInteger("" + chunk);
                if (days - lastDay > 0) {
                    removeDecorations(event.getWorld(), event.getChunk());
                    spawnDecorations(event.getWorld(), event.getChunk());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onChunkSave(ChunkDataEvent.Save event) {
        if (Mine.BY_ID.containsKey(event.getWorld().provider.getDimension())) {
            if ((event.getChunk().x < 0 || event.getChunk().z < 0)) return;
            NBTTagCompound tag = NBTHelper.getOrCreateTag(event.getData(), "LastUpdate");
            long chunk = ChunkPos.asLong(event.getChunk().x, event.getChunk().z);
            int days = TimeHelper.getElapsedDays(event.getWorld().getWorldTime());
            tag.setInteger("" + chunk, days);
        }
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        if (Mine.BY_ID.containsKey(event.getWorld().provider.getDimension())) {
            for (Tier tier: Mine.BY_ID.get(event.getWorld().provider.getDimension()).TIERS) {
                tier.map.clear();//Clear everything as we're a new world and stuff
            }
        }
    }
}
