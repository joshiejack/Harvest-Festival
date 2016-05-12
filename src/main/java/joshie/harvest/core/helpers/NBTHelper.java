package joshie.harvest.core.helpers;

import joshie.harvest.player.tracking.TrackingData.HolderMapStack;
import joshie.harvest.player.tracking.TrackingData.HolderStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class NBTHelper {
    private static <C extends Collection, H extends HolderStack> C readCollection(Class<C> c, Class<H> h, NBTTagList list) {
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

    public static <H extends HolderStack> ArrayList<H> readList(Class<H> h, NBTTagList list) {
        return readCollection(ArrayList.class, h, list);
    }

    public static <H extends HolderStack> HashSet<H> readHashSet(Class<H> h, NBTTagList list) {
        return readCollection(HashSet.class, h, list);
    }

    public static <C extends Collection<? extends HolderStack>> NBTTagList writeCollection(C set) {
        NBTTagList list = new NBTTagList();
        if (set != null) {
            for (HolderStack stack : set) {
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

    public interface ISaveable {
        public void readFromNBT(NBTTagCompound tag);
        public void writeToNBT(NBTTagCompound tag);
    }
}
