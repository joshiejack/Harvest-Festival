package joshie.harvest.mining;

import joshie.harvest.core.HFTrackers;
import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

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

    @Override
    @SuppressWarnings("unchecked")
    public void onWorldSave() {
        //TODO: Remove in 0.6+
        if (worldObj.getWorldInfo().getDimensionData(MINE_WORLD).hasKey("MineManager")) {
            ((Map<DimensionType, NBTTagCompound>)ReflectionHelper.getPrivateValue(WorldInfo.class, worldObj.getWorldInfo(), "dimensionData", "field_186348_N", "N")).remove(MINE_WORLD);
        }
    }
}
