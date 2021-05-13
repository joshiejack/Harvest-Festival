package uk.joshiejack.horticulture.world;

import uk.joshiejack.horticulture.block.BlockSapling;
import uk.joshiejack.horticulture.HorticultureConfig;
import uk.joshiejack.horticulture.world.gen.feature.WorldGenFruitTree;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class FruitTreeGenerator implements IWorldGenerator {
    private static final WorldGenFruitTree APPLE = new WorldGenFruitTree(BlockSapling.Sapling.APPLE);
    private static final WorldGenFruitTree BANANA = new WorldGenFruitTree(BlockSapling.Sapling.BANANA);

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        int x = chunkX * 16 + 8 + random.nextInt(16);
        int z = chunkZ * 16 + 8 + random.nextInt(16);
        BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z));
        Biome biome = world.getBiomeForCoordsBody(pos);
        if (HorticultureConfig.enableAppleTrees) {
            if ((biome.getTempCategory() == Biome.TempCategory.MEDIUM && BiomeDictionary.hasType(biome, BiomeDictionary.Type.PLAINS) && random.nextInt(16) == 0)
                    || (BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST) && random.nextInt(6) == 0)) {
                APPLE.generate(world, random, pos);
            }
        }

        if (HorticultureConfig.enableBananaTrees) {
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE) && random.nextInt(6) == 0) {
                BANANA.generate(world, random, pos);
            }
        }
    }
}
