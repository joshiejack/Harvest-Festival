package joshie.harvest.mining;

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import joshie.harvest.core.helpers.NBTHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;

import static joshie.harvest.mining.MiningChunk.FLOOR_HEIGHT;

public class MineManager {
    private static final int CHUNK_BOUNDARY = 3;
    private WorldServer world;
    private TIntIntMap widths = new TIntIntHashMap();
    private TIntIntMap lengths = new TIntIntHashMap();
    private TIntIntMap startX = new TIntIntHashMap();
    private TIntIntMap startZ = new TIntIntHashMap();

    public int getDimension(TIntIntMap map, int index, boolean first, int missing) {
        map.putIfAbsent(index, missing);
        int theWidth = map.get(index);
        if (first) return theWidth;
        else return theWidth - 16;
    }

    private int getIndex(int chunkX, int chunkY, int chunkZ) {
        int x = (int) Math.floor(chunkX / CHUNK_BOUNDARY); //3x3 Chunks
        int y = (int) Math.floor(chunkY / FLOOR_HEIGHT); // Height
        int z = (int) Math.floor(chunkZ / CHUNK_BOUNDARY); //3x3 Chunks
        int result = x;
        result = 31 * result + z;
        result = 31 * result + y;
        return result;
    }

    public int getWidth(int chunkX, int chunkZ, int chunkY) {
        return getDimension(widths, getIndex(chunkX, chunkY, chunkZ), chunkX % CHUNK_BOUNDARY == 0, 8 + world.rand.nextInt(24));
    }

    public int getLength(int chunkX, int chunkZ, int chunkY) {
        return getDimension(lengths, getIndex(chunkX, chunkY, chunkZ), chunkZ % CHUNK_BOUNDARY == 0, 8 + world.rand.nextInt(24));
    }

    public int getStartX(int chunkX, int chunkY, int chunkZ, int width) {
        return chunkX % CHUNK_BOUNDARY == 0 ? getDimension(startZ, getIndex(chunkX, chunkY, chunkZ), chunkX % CHUNK_BOUNDARY == 0, world.rand.nextInt(7)) : 0;
    }

    public int getStartZ(int chunkX, int chunkY, int chunkZ, int length) {
        return chunkZ % CHUNK_BOUNDARY == 0 ? getDimension(startZ, getIndex(chunkX, chunkY, chunkZ), chunkZ % CHUNK_BOUNDARY == 0, world.rand.nextInt(7)) : 0;
    }

    public MineManager(WorldServer world) {
        this.world = world;
    }

    public void readFromNBT(NBTTagCompound tag) {
        widths = NBTHelper.readMap(tag.getTagList("Widths", 10));
        lengths = NBTHelper.readMap(tag.getTagList("Lengths", 10));
        startX = NBTHelper.readMap(tag.getTagList("StartX", 10));
        startZ = NBTHelper.readMap(tag.getTagList("StartZ", 10));
    }
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setTag("Widths", NBTHelper.writeIntMap(widths));
        tag.setTag("Lengths", NBTHelper.writeIntMap(lengths));
        tag.setTag("StartX", NBTHelper.writeIntMap(startX));
        tag.setTag("StartZ", NBTHelper.writeIntMap(startZ));
        return tag;
    }
}
