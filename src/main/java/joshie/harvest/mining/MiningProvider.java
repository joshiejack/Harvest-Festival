package joshie.harvest.mining;

import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.mining.HFMining.MINE_WORLD;

public class MiningProvider extends WorldProvider {
    private MineManager manager = null;

    @Override
    public void createBiomeProvider() {
        biomeProvider = new BiomeProviderSingle(Biomes.VOID);
        hasNoSky = true;
        if (worldObj instanceof WorldServer) {
            manager = (MineManager) worldObj.getPerWorldStorage().getOrLoadData(MineManager.class, "mine_data");
            if (manager == null) {
                manager = new MineManager("mine_data");
                worldObj.getPerWorldStorage().setData("mine_data", manager);
            }
        }
    }

    @Override
    public IChunkGenerator createChunkGenerator() {
        return new MiningChunk(worldObj, worldObj.getSeed(), manager);
    }

    @Override
    public BlockPos getSpawnCoordinate() {
        return getSpawnCoordinateForMine(0);
    }

    public BlockPos getSpawnCoordinateForMine(int mineID) {
        return manager.getSpawnCoordinateForMine(mineID);
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
    public void onWorldSave() {
        if (manager != null) {
            worldObj.getWorldInfo().setDimensionData(MINE_WORLD, manager.writeToNBT(new NBTTagCompound()));
        }
    }
}
