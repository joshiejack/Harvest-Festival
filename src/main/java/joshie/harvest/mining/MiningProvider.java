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
        NBTTagCompound tag = worldObj.getWorldInfo().getDimensionData(MINE_WORLD);
        manager = worldObj instanceof WorldServer ? new MineManager((WorldServer)worldObj) : null;
        if (manager != null && tag != null) {
            manager.readFromNBT(tag);
        }
    }

    @Override
    public IChunkGenerator createChunkGenerator() {
        return new MiningChunk(worldObj, worldObj.getSeed(), manager);
    }

    @Override
    public BlockPos getSpawnCoordinate() {
        return manager.getSpawnCoordinate();
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
