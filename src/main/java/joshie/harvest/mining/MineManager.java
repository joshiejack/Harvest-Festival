package joshie.harvest.mining;

import gnu.trove.map.TIntLongMap;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntLongHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import joshie.harvest.core.helpers.NBTHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;

public class MineManager {
    public static final int CHUNK_BOUNDARY = 10;
    private WorldServer world;
    private TIntLongMap seeds = new TIntLongHashMap();
    private TIntObjectMap<TIntObjectMap<IBlockState[][]>> generation = new TIntObjectHashMap();
    private TIntObjectMap<int[]> coordinates = new TIntObjectHashMap();

    public MineManager(WorldServer world) {
        this.world = world;
    }

    public TIntObjectMap<IBlockState[][]> getStateMap(int mapIndex) {
        return generation.get(mapIndex);
    }

    public void putStateMap(int mapIndex, TIntObjectMap<IBlockState[][]>  map) {
        generation.put(mapIndex, map);
    }

    public boolean containsStateKey(int mapIndex) {
        return generation.containsKey(mapIndex);
    }

    public boolean containsCoordinatesKey(int mapIndex) {
        return coordinates.containsKey(mapIndex);
    }

    public void putCoordinates(int mapIndex, int[] coordinates) {
        this.coordinates.put(mapIndex, coordinates);
    }

    public int getCoordinates(int mapIndex, int position) {
        return coordinates.get(mapIndex)[position];
    }

    public long getSeed(int mapIndex) {
        seeds.putIfAbsent(mapIndex, world.rand.nextLong());
        return seeds.get(mapIndex);
    }

    private int getDoneID(int chunkX, int chunkZ) {
        return chunkX << 12 | chunkZ << 8;
    }

    public void readFromNBT(NBTTagCompound tag) {
        seeds = NBTHelper.readLongMap(tag.getTagList("Seeds", 10));
    }
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setTag("Seeds", NBTHelper.writeLongMap(seeds));
        return tag;
    }
}
