package joshie.harvest.core.helpers;

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import joshie.harvest.blocks.tiles.TileHarvest;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.player.tracking.TrackingData.AbstractHolder;
import joshie.harvest.player.tracking.TrackingData.HolderMapStack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class NBTHelper {
    public static void copyTileData(TileHarvest tile, World world, BlockPos pos, IBlockState state) {
        NBTTagCompound data = tile.writeToNBT(new NBTTagCompound()); //Save the old data
        world.setBlockState(pos, state, 2);
        TileHarvest tile2 = (TileHarvest) world.getTileEntity(pos);
        tile2.readFromNBT(data); //Copy over the data as we change the state
        PacketHandler.sendRefreshPacket(tile2);
    }

    private static <C extends Collection, H extends AbstractHolder> C readCollection(Class<C> c, Class<H> h, NBTTagList list) {
        try {
            C collection = c.newInstance();
            for (int i = 0; i < list.tagCount(); i++) {
                H stack = h.newInstance();
                stack.readFromNBT(list.getCompoundTagAt(i));
                if (stack.getKey() != null) {
                    collection.add(stack);
                }
            }

            return collection;
        } catch (Exception e) { e.printStackTrace(); }

        //Whatever
        try {
            return c.newInstance();
        } catch (Exception e) { return  null; }
    }

    public static <H extends AbstractHolder> ArrayList<H> readList(Class<H> h, NBTTagList list) {
        return readCollection(ArrayList.class, h, list);
    }

    public static <H extends AbstractHolder> HashSet<H> readHashSet(Class<H> h, NBTTagList list) {
        return readCollection(HashSet.class, h, list);
    }

    public static <C extends Collection<? extends AbstractHolder>> NBTTagList writeCollection(C set) {
        NBTTagList list = new NBTTagList();
        if (set != null) {
            for (AbstractHolder stack : set) {
                NBTTagCompound tag = new NBTTagCompound();
                stack.writeToNBT(tag);
                list.appendTag(tag);
            }
        }

        return list;
    }

    public static <C extends ISaveable, I extends ISaveable> NBTBase writeMap(HashMap<C, I> map) {
        NBTTagList list = new NBTTagList();
        for (C c: map.keySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            c.writeToNBT(tag);
            map.get(c).writeToNBT(tag);
            list.appendTag(tag);
        }

        return list;
    }

    public static <K extends HolderMapStack, V> HashMap<K, V> readMap(Class<K> c, NBTTagList list) {
        HashMap<K, V> map = new HashMap();
        for (int j = 0; j < list.tagCount(); j++) {
            NBTTagCompound tag = list.getCompoundTagAt(j);
            try {
                K holder = c.newInstance();
                holder.readFromNBT(tag);
                if (holder.getKey() != null) {
                    map.put(holder, (V) holder.getValue());
                }
            } catch (Exception e) {}
        }

        return map;
    }

    public static NBTTagList writeIntMap(TIntIntMap map) {
        NBTTagList list = new NBTTagList();
        for (int key: map.keys()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("Key", key);
            tag.setInteger("Value", map.get(key));
            list.appendTag(tag);
        }

        return list;
    }

    public static TIntIntMap readMap(NBTTagList list) {
        TIntIntMap map = new TIntIntHashMap();
        for (int j = 0; j < list.tagCount(); j++) {
            NBTTagCompound tag = list.getCompoundTagAt(j);
            try {
                int key = tag.getInteger("Key");
                int value = tag.getInteger("Value");
                map.put(key, value);
            } catch (Exception e) {}
        }

        return map;
    }

    public static BlockPos readBlockPos(String prefix, NBTTagCompound tag) {
        return tag.hasKey(prefix + "X")? new BlockPos(tag.getInteger(prefix + "X"), tag.getInteger(prefix + "Y"), tag.getInteger(prefix + "Z")) : BlockPos.ORIGIN;
    }

    public static void writeBlockPos(String prefix, NBTTagCompound tag, BlockPos pos) {
        if (pos != null) {
            tag.setInteger(prefix + "X", pos.getX());
            tag.setInteger(prefix + "Y", pos.getY());
            tag.setInteger(prefix + "Z", pos.getZ());
        }
    }

    public static UUID readUUID(String prefix, NBTTagCompound nbt) {
        String key = prefix + "UUID";
        if (nbt.hasKey(key)) {
            return UUID.fromString(nbt.getString(key));
        } else return UUID.randomUUID();
    }

    public static void writeUUID(String prefix, NBTTagCompound nbt, UUID uuid) {
        if (uuid != null) {
            nbt.setString(prefix + "UUID", uuid.toString());
        }
    }

    public interface ISaveable {
        void readFromNBT(NBTTagCompound tag);
        NBTTagCompound writeToNBT(NBTTagCompound tag);
    }
}
