package uk.joshiejack.gastronomy.world;

import uk.joshiejack.gastronomy.block.BlockNature;
import uk.joshiejack.gastronomy.block.GastronomyBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;

import javax.annotation.Nonnull;
import java.util.Random;

public class WorldGenNature extends WorldGenerator {
    private final Biome.TempCategory category;
    private final BiomeDictionary.Type[] types;
    private final IBlockState state;

    WorldGenNature(BlockNature.Nature type, Biome.TempCategory category) {
        super(false);
        this.category = category;
        this.types = new BiomeDictionary.Type[0];
        this.state = GastronomyBlocks.NATURE.getStateFromEnum(type);
    }

    WorldGenNature(BlockNature.Nature type, BiomeDictionary.Type... typeBiome) {
        super(false);
        this.category = null;
        this.types = typeBiome;
        this.state = GastronomyBlocks.NATURE.getStateFromEnum(type);
    }

    public boolean isValidBiome(Biome biome) {
        if (category != null) return biome.getTempCategory() == category;
        else {
            for (BiomeDictionary.Type type: types) {
                if (!BiomeDictionary.hasType(biome, type)) return false;
            }

            return true;
        }
    }

    @Override
    public boolean generate(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos position) {
        for (int i = 0; i < 64; ++i) {
            BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
            if (worldIn.isBlockLoaded(blockpos) && worldIn.isAirBlock(blockpos)
                    && (!worldIn.provider.isNether() || blockpos.getY() < 255)
                    && GastronomyBlocks.NATURE.canBlockStay(worldIn, blockpos, state) && isValidBiome(worldIn.getBiome(blockpos))) {
                worldIn.setBlockState(blockpos, state, 2);
            }
        }

        return true;
    }
}
