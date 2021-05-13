package uk.joshiejack.harvestcore.world.mine.dimension;

import uk.joshiejack.harvestcore.world.mine.Mine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.Nonnull;

public class MineWorldProvider extends WorldProvider {
    @Override
    public void init() {
        biomeProvider = new MineBiomeProvider(Mine.BY_ID.get(world.provider.getDimension()));
        hasSkyLight = false;
    }

    @Override
    @Nonnull
    public IChunkGenerator createChunkGenerator() {
        return new MineChunkGenerator(world);
    }

    @Nonnull
    @Override
    public DimensionType getDimensionType() {
        return Mine.BY_ID.get(world.provider.getDimension()).type;
    }

    @Nonnull
    @Override
    public Biome getBiomeForCoords(@Nonnull BlockPos pos) {
        return biomeProvider.getBiome(pos);
    }

    @Override
    public boolean canMineBlock(@Nonnull EntityPlayer player, @Nonnull BlockPos pos) {
        return false;
    }

    @Override
    public boolean isSurfaceWorld()
    {
        return false;
    }

    @Override
    public boolean canRespawnHere()
    {
        return false;
    }
}
