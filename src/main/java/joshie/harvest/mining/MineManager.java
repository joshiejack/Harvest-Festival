package joshie.harvest.mining;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.util.HFEvents;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

import static joshie.harvest.mining.MiningTicker.MAX_FLOORS;

@HFEvents
public class MineManager extends WorldSavedData {
    public static final int CHUNK_BOUNDARY = 10;
    private static final TIntObjectMap<TIntObjectMap<IBlockState[][]>> generation = new TIntObjectHashMap<>();
    private static final TIntObjectMap<int[]> coordinates = new TIntObjectHashMap<>();
    private TIntObjectMap<TIntObjectMap<BlockPos>> portalCoordinates = new TIntObjectHashMap<>();

    public MineManager(String string) {
        super(string);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        portalCoordinates = NBTHelper.readPositionCollection(tag.getTagList("PortalCoordinates", 10));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setTag("PortalCoordinates", NBTHelper.writePositionCollection(portalCoordinates));
        return tag;
    }

    public static boolean areCoordinatesGenerated(World world, int mineID, int floor) {
        return HFTrackers.getMineManager(world).getCoordinateMap(mineID).containsKey(floor);
    }

    public BlockPos getSpawnCoordinateForMine(World world, int mineID, int floor) {
        BlockPos ret = getCoordinateMap(mineID).get(floor);
        if (ret == null) {
            BlockPos pos = new BlockPos(Math.floor(floor / MAX_FLOORS) * CHUNK_BOUNDARY * 16, (floor - 1) % MAX_FLOORS == 0 ? 247 : 1, mineID * CHUNK_BOUNDARY * 16);
            for (int x = 0; x < 16 * CHUNK_BOUNDARY; x++) {
                for (int z = 0; z < 16 * CHUNK_BOUNDARY; z++) {
                    BlockPos toCheck = pos.add(x, 0, z);
                    IBlockState state = world.getBlockState(toCheck);
                    if (state.getBlock() == HFMining.PORTAL && state.getActualState(world, toCheck).getValue(HFMining.PORTAL.property).isCentre()) {
                        return toCheck;
                    }
                }
            }

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
        markDirty();
    }

    static TIntObjectMap<IBlockState[][]> getStateMap(int mapIndex) {
        return MineManager.generation.get(mapIndex);
    }

     static void putStateMap(int mapIndex, TIntObjectMap<IBlockState[][]>  map) {
         MineManager.generation.put(mapIndex, map);
    }

    static boolean containsStateKey(int mapIndex) {
        return MineManager.generation.containsKey(mapIndex);
    }

    static boolean containsCoordinatesKey(int mapIndex) {
        return MineManager.coordinates.containsKey(mapIndex);
    }

    static void putCoordinates(int mapIndex, int[] coordinates) {
        MineManager.coordinates.put(mapIndex, coordinates);
    }

    static int getCoordinates(int mapIndex, int position) {
        return MineManager.coordinates.get(mapIndex)[position];
    }
}
