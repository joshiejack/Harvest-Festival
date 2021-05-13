package uk.joshiejack.harvestcore.world.mine.dimension;

import uk.joshiejack.harvestcore.world.mine.Mine;
import uk.joshiejack.harvestcore.world.mine.MineHelper;
import uk.joshiejack.harvestcore.world.mine.dimension.decorators.DecoratorLoot;
import uk.joshiejack.harvestcore.world.mine.dimension.wrappers.AbstractDecoratorWrapper;
import uk.joshiejack.harvestcore.world.mine.dimension.wrappers.DecoratorWrapperChunkPrimer;
import uk.joshiejack.harvestcore.world.mine.tier.Tier;
import uk.joshiejack.harvestcore.world.storage.SavedData;
import uk.joshiejack.penguinlib.util.BlockStates;
import uk.joshiejack.penguinlib.util.world.EmptyChunkGenerator;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;

import javax.annotation.Nonnull;
import java.util.List;

public class MineChunkGenerator extends EmptyChunkGenerator {
    public static final boolean ENABLE_DEBUG_MODE = true;
    public static final int CHUNKS_PER_SECTION = 10;
    public static final int MAX_XZ_PER_SECTION = CHUNKS_PER_SECTION * 16;
    private Biome[] biomesForGeneration;

    MineChunkGenerator(World world) {
        super(world);
    }

    @Override
    @Nonnull
    public List<Biome.SpawnListEntry> getPossibleCreatures(@Nonnull EnumCreatureType creatureType, @Nonnull BlockPos pos) {
        int tierNumber = world.getChunk(pos).z / CHUNKS_PER_SECTION; //The Tier of this section of the mine
        Tier tier = Mine.BY_ID.get(world.provider.getDimension()).getTierFromInt(tierNumber);
        return tier.getSpawnableList(creatureType);
    }

    @Nonnull
    @Override
    public Chunk generateChunk(int x, int z) {
        rand.setSeed((long) x * 341873128712L + (long) z * 132897987541L);
        ChunkPrimer chunkprimer = new ChunkPrimer();
        biomesForGeneration = world.getBiomeProvider().getBiomes(biomesForGeneration, x * 16, z * 16, 16, 16);
        boolean negative = (x < 0 || z < 0);
        if (negative) MineHelper.fillChunkWith(BlockStates.BEDROCK, chunkprimer);
        else setBlocksInChunk(chunkprimer, x, z);
        Chunk chunk = new Chunk(world, chunkprimer, x, z);
        byte[] abyte = chunk.getBiomeArray();
        for (int i = 0; i < abyte.length; ++i) {
            abyte[i] = (byte) Biome.getIdForBiome(biomesForGeneration[i]);
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    private void setBlocksInChunk(ChunkPrimer primer, int chunkX, int chunkZ) {
        int id = chunkX / CHUNKS_PER_SECTION; //The ID of this mine
        int tierNumber = chunkZ / CHUNKS_PER_SECTION; //The Tier of this section of the mine
        long generateID = new BlockPos(id, 0, tierNumber).toLong();
        int sectionX = chunkX % CHUNKS_PER_SECTION;
        int sectionZ = chunkZ % CHUNKS_PER_SECTION;
        //floor 1 = 240, 240/6 = 40   41 - y / 6
        Tier tier = Mine.BY_ID.get(world.provider.getDimension()).getTierFromInt(tierNumber);
        MineData data = SavedData.getMineData(world, world.provider.getDimension());
        tier.setBlocksInChunk(primer, generateID, sectionX, sectionZ,  data.getElevatorsForID(id),  data.getPortalsForID(id));
        //Spawn decorations
        for (int y = 2; y < 242; y+= 6) {
            int floor = MineHelper.getRelativeFloor(y);
            int actualFloor = MineHelper.getFloorFromCoordinates(chunkZ, floor);
            AbstractDecoratorWrapper wrapper = new DecoratorWrapperChunkPrimer(primer, tier, world.rand, floor);
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (world.rand.nextDouble() <= MineDecorator.getDecorateChance(actualFloor)) {
                        if (wrapper.getBlockState(pos) == BlockStates.AIR && wrapper.getBlockState(pos.down()).isFullCube()) {
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
