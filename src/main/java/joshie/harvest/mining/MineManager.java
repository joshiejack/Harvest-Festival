package joshie.harvest.mining;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import joshie.harvest.core.helpers.NBTHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class MineManager  {
    public static final int CHUNK_BOUNDARY = 10;
    private TIntObjectMap<TIntObjectMap<IBlockState[][]>> generation = new TIntObjectHashMap();
    private TIntObjectMap<int[]> coordinates = new TIntObjectHashMap();
    private TIntObjectMap<TIntObjectMap<BlockPos>> portalCoordinates = new TIntObjectHashMap<>();

    public boolean areCoordinatesGenerated(int mineID, int floor) {
        return getCoordinateMap(mineID).containsKey(floor);
    }

    public BlockPos getSpawnCoordinateForMine(int mineID, int floor) {
        BlockPos ret = getCoordinateMap(mineID).get(floor);
        if (ret == null) {
            return new BlockPos(0, 254, mineID * CHUNK_BOUNDARY * 16);
        }

        return ret;
    }

    private TIntObjectMap<BlockPos> getCoordinateMap(int mineID) {
        TIntObjectMap<BlockPos> map = portalCoordinates.get(mineID);
        if (map == null) {
            map = new TIntObjectHashMap<>();
            portalCoordinates.put(mineID, map);
        }

        return map;
    }

    public void setSpawnForMine(int mineID, int floor, int x, int y, int z) {
        getCoordinateMap(mineID).putIfAbsent(floor, new BlockPos(x, y, z));
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

    public MineManager(NBTTagCompound tag) {
        portalCoordinates = NBTHelper.readPositionCollection(tag.getTagList("PortalCoordinates", 10));
    }

    public NBTTagCompound getCompound() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("PortalCoordinates", NBTHelper.writePositionCollection(portalCoordinates));
        return tag;
    }
}
