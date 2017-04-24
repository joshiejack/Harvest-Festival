package joshie.harvest.mining.gen;

import joshie.harvest.core.HFTrackers;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

import static joshie.harvest.mining.HFMining.MINE_WORLD;

public class MiningProvider extends WorldProvider {
    @Override
    public void init() {
        biomeProvider = new BiomeProviderSingle(Biomes.VOID);
        hasNoSky = true;
    }

    @Override
    @Nonnull
    public IChunkGenerator createChunkGenerator() {
        return new MiningChunk(world, world.getSeed());
    }

    @Override
    @Nonnull
    public BlockPos getSpawnCoordinate() {
        return getSpawnCoordinateForMine(0, 1);
    }

    public BlockPos getSpawnCoordinateForMine(int mineID, int floor) {
        return HFTrackers.getMineManager(world).getSpawnCoordinateForMine(world, mineID, floor);
    }

    public boolean areCoordinatesGenerated(int mineID, int floor) {
        return MineManager.areCoordinatesGenerated(world, mineID, floor);
    }

    public void onTeleportToMine(int mineID) {
        HFTrackers.getMineManager(world).onTeleportToMine(world, mineID);
    }

    @SideOnly(Side.CLIENT)
    public boolean isSkyColored() {
        return false;
    }

    @Override
    public boolean canRespawnHere() {
        return false;
    }

    @Override
    public boolean isSurfaceWorld() {
        return false;
    }

    @Override
    @Nonnull
    public DimensionType getDimensionType() {
        return MINE_WORLD;
    }
}
