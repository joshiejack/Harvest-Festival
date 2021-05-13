package uk.joshiejack.harvestcore.world.mine.dimension;

import uk.joshiejack.harvestcore.world.mine.Mine;
import uk.joshiejack.harvestcore.world.mine.tier.Tier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static uk.joshiejack.harvestcore.world.mine.dimension.MineChunkGenerator.CHUNKS_PER_SECTION;

public class MineBiomeProvider extends BiomeProvider {
    private final Mine mine;

    public MineBiomeProvider(Mine mine) {
        this.mine = mine;
    }

    @Nonnull
    @Override
    public Biome getBiome(@Nonnull BlockPos pos) {
        return mine.getTierFromInt((pos.getZ() >> 4) / CHUNKS_PER_SECTION).getBiome();
    }

    @Nonnull
    @Override
    public Biome[] getBiomesForGeneration(@Nullable Biome[] biomes, int x, int z, int width, int height) {
        if (biomes == null || biomes.length < width * height)
        {
            biomes = new Biome[width * height];
        }

        Tier tier = mine.getTierFromInt((z >> 4) / CHUNKS_PER_SECTION);
        Arrays.fill(biomes, 0, width * height, tier.getBiome());
        return biomes;
    }

    @Nonnull
    @Override
    public Biome[] getBiomes(@Nullable Biome[] oldBiomeList, int x, int z, int width, int depth)  {
        if (oldBiomeList == null || oldBiomeList.length < width * depth) {
            oldBiomeList = new Biome[width * depth];
        }

        Tier tier = mine.getTierFromInt((z >> 4) / CHUNKS_PER_SECTION);
        Arrays.fill(oldBiomeList, 0, width * depth, tier.getBiome());
        return oldBiomeList;
    }

    @Nonnull
    @Override
    public Biome[] getBiomes(@Nullable Biome[] listToReuse, int x, int z, int width, int length, boolean cacheFlag) {
        return this.getBiomes(listToReuse, x, z, width, length);
    }

    @Nullable
    @Override
    public BlockPos findBiomePosition(int x, int z, int range, @Nonnull List<Biome> biomes, @Nonnull Random random) {
        Tier tier = mine.getTierFromInt((z >> 4) / CHUNKS_PER_SECTION);
        return biomes.contains(tier.getBiome()) ? new BlockPos(x - range + random.nextInt(range * 2 + 1), 0, z - range + random.nextInt(range * 2 + 1)) : null;
    }

    @Override
    public boolean areBiomesViable(int x, int z, int radius, @Nonnull List<Biome> allowed) {
        Tier tier = mine.getTierFromInt((z >> 4) / CHUNKS_PER_SECTION);
        return allowed.contains(tier.getBiome());
    }
}
