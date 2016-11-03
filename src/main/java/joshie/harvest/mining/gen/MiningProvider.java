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

import static joshie.harvest.mining.HFMining.MINE_WORLD;

public class MiningProvider extends WorldProvider {
    @Override
    public void createBiomeProvider() {
        biomeProvider = new BiomeProviderSingle(Biomes.VOID);
        hasNoSky = true;
    }

    @Override
    public IChunkGenerator createChunkGenerator() {
        return new MiningChunk(worldObj, worldObj.getSeed());
    }

    @Override
    public BlockPos getSpawnCoordinate() {
        return getSpawnCoordinateForMine(0, 1);
    }

    public BlockPos getSpawnCoordinateForMine(int mineID, int floor) {
        return HFTrackers.getMineManager(worldObj).getSpawnCoordinateForMine(mineID, floor);
    }

    public boolean areCoordinatesGenerated(int mineID, int floor) {
        return MineManager.areCoordinatesGenerated(worldObj, mineID, floor);
    }

    public void onTeleportToMine(int mineID) {
        HFTrackers.getMineManager(worldObj).onTeleportToMine(worldObj, mineID);
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
    public DimensionType getDimensionType() {
        return MINE_WORLD;
    }
}
