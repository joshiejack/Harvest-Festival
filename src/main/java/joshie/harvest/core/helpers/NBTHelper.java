package joshie.harvest.core.helpers;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import joshie.harvest.core.base.tile.TileHarvest;
import joshie.harvest.core.util.holders.AbstractHolder;
import joshie.harvest.core.util.interfaces.INBTSerializableMap;
import joshie.harvest.core.util.interfaces.INBTWriteable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class NBTHelper {
    public static void copyTileData(TileHarvest tile, World world, BlockPos pos, IBlockState state) {
        NBTTagCompound data = tile.writeToNBT(new NBTTagCompound()); //Save the old data
        world.setBlockState(pos, state, 2);
        TileHarvest tile2 = (TileHarvest) world.getTileEntity(pos);
        tile2.readFromNBT(data); //Copy over the data as we change the state
        MCServerHelper.markTileForUpdate(tile2);
    }

    @SuppressWarnings("unchecked")
    private static <C extends Collection, H extends AbstractHolder> C readCollection(Class<C> c, Class<H> h, NBTTagList list) {
        try {
            C collection = c.newInstance();
            for (int i = 0; i < list.tagCount(); i++) {
                collection.add(h.getMethod("readFromNBT", NBTTagCompound.class).invoke(null, list.getCompoundTagAt(i)));
            }

            return collection;
        } catch (Exception e) { e.printStackTrace(); }

        //Whatever
        try {
            return c.newInstance();
        } catch (Exception e) { return  null; }
    }

    @SuppressWarnings("unchecked")
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

    public static NBTTagList writePositionMap(TIntObjectMap<BlockPos> map) {
        NBTTagList list = new NBTTagList();
        for (int key: map.keys()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("Key", key);
            writeBlockPos("Value", tag, map.get(key));
            list.appendTag(tag);
        }

        return list;
    }

    public static NBTTagList writePositionCollection(TIntObjectMap<TIntObjectMap<BlockPos>> mapMap) {
        NBTTagList idList = new NBTTagList();
        for (int mapKey: mapMap.keys()) {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setInteger("Key", mapKey);
            compound.setTag("Value", writePositionMap(mapMap.get(mapKey)));
            idList.appendTag(compound);
        }

        return idList;
    }

    public static TIntObjectMap<BlockPos> readPositionMap(NBTTagList list) {
        TIntObjectMap<BlockPos> ret = new TIntObjectHashMap<>();
        for (int j = 0; j < list.tagCount(); j++) {
            NBTTagCompound tag = list.getCompoundTagAt(j);
            try {
                int key = tag.getInteger("Key");
                BlockPos value = readBlockPos("Value", tag);
                ret.put(key, value);
            } catch (Exception e) { e.printStackTrace(); }
        }

        return ret;
    }

    public static TIntObjectMap<TIntObjectMap<BlockPos>> readPositionCollection(NBTTagList list) {
        TIntObjectMap<TIntObjectMap<BlockPos>> mapMap = new TIntObjectHashMap<>();
        for (int j = 0; j < list.tagCount(); j++) {
            NBTTagCompound tag = list.getCompoundTagAt(j);
            try {
                int key = tag.getInteger("Key");
                TIntObjectMap<BlockPos> map = readPositionMap(tag.getTagList("Value", 10));
                mapMap.put(key, map);
            } catch (Exception e) {}
        }

        return mapMap;
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

    public static Set<ResourceLocation> readResourceSet(NBTTagCompound nbt, String name) {
        NBTTagList list = nbt.getTagList(name, 8);
        Set<ResourceLocation> set = new HashSet<>();
        for (int i = 0; i < list.tagCount(); i++) {
            set.add(new ResourceLocation(list.getStringTagAt(i)));
        }

        return set;
    }

    public static NBTTagList writeResourceSet(Set<ResourceLocation> resources) {
        NBTTagList list = new NBTTagList();
        for (ResourceLocation resource: resources) {
            list.appendTag(new NBTTagString(resource.toString()));
        }

        return list;
    }

    public static Set<UUID> readUUIDSet(NBTTagCompound nbt, String name) {
        NBTTagList list = nbt.getTagList(name, 8);
        Set<UUID> set = new HashSet<>();
        for (int i = 0; i < list.tagCount(); i++) {
            set.add(UUID.fromString(list.getStringTagAt(i)));
        }

        return set;
    }

    public static void writeUUIDSet(NBTTagCompound nbt, String name, Set<UUID> uuids) {
        NBTTagList list = new NBTTagList();
        for (UUID uuid: uuids) {
            list.appendTag(new NBTTagString(uuid.toString()));
        }

        nbt.setTag(name, list);
    }

    public static ItemStack readItemStack(NBTTagCompound nbt) {
        Item item = Item.getByNameOrId(nbt.getString("id"));
        if (item == null) return null; //DIE!
        ItemStack stack = new ItemStack(item);
        stack.stackSize = nbt.getInteger("Count");
        int damage = nbt.getShort("Damage");

        if (damage < 0){
            damage = 0;
        }

        stack.setItemDamage(damage);
        if (nbt.hasKey("tag", 10)) {
            stack.setTagCompound(nbt.getCompoundTag("tag"));
            stack.getItem().updateItemStackNBT(stack.getTagCompound());
        } else stack.setTagCompound(null);

        return stack;
    }

    public static NBTTagCompound writeItemStack(ItemStack stack, NBTTagCompound nbt) {
        ResourceLocation resourcelocation = Item.REGISTRY.getNameForObject(stack.getItem());
        nbt.setString("id", resourcelocation == null ? "minecraft:air" : resourcelocation.toString());
        nbt.setInteger("Count", stack.stackSize);
        nbt.setShort("Damage", (short)stack.getItemDamage());

        if (stack.getTagCompound() != null) {
            nbt.setTag("tag", stack.getTagCompound());
        }

        return nbt;
    }

    @SuppressWarnings("unchecked")
    public static <R, T> void readMap(String name, Class<? extends INBTSerializableMap> clazz, Map<R, T> map, NBTTagCompound nbt) {
        NBTTagList list = nbt.getTagList(name, 10);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            try {
                INBTSerializableMap object = clazz.newInstance();
                if (object != null) {
                    object.deserializeNBT(tag);
                    object.buildMap(map);
                }
            } catch (InstantiationException | IllegalAccessException ex) {}
        }
    }

    public static <R, T extends INBTSerializableMap> void writeMap(String name, NBTTagCompound nbt, Map<R, T> map) {
        NBTTagList list = new NBTTagList();
        for (T entry: map.values()) {
            list.appendTag(entry.serializeNBT());
        }

        nbt.setTag(name, list);
    }

    @SuppressWarnings("unchecked")
    public static <L extends Collection, B> void readList(String name, Class<B> clazz, L list, NBTTagCompound nbt) {
        try {
            NBTTagList currently = nbt.getTagList(name, 10);
            for (int i = 0; i < currently.tagCount(); i++) {
                list.add(clazz.getMethod("readFromNBT", NBTTagCompound.class).invoke(null, currently.getCompoundTagAt(i)));
            }
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {/**/}
    }

    public static <T extends INBTWriteable> void writeList(String name, NBTTagCompound nbt, List<T> theList) {
        NBTTagList list = new NBTTagList();
        for (T entry: theList) {
            NBTTagCompound tag = new NBTTagCompound();
            entry.writeToNBT(tag);
            list.appendTag(tag);
        }

        nbt.setTag(name, list);
    }

    public static NBTTagCompound getLastTickData(NBTTagCompound tag) {
        if (tag.hasKey("LastTickData")) return tag.getCompoundTag("LastTickData");
        else {
            NBTTagCompound data = new NBTTagCompound();
            tag.setTag("LastTickData", data);
            return data;
        }
    }
}
