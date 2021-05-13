package uk.joshiejack.penguinlib.util.helpers.minecraft;

import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.INBTSerializable;
import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.util.PenguinRegistry;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class NBTHelper {
    public static NBTTagCompound getNBTWithValue(String name, Object object) {
        NBTTagCompound tag = new NBTTagCompound();
        if (object instanceof Long) {
            tag.setLong(name, (Long) object);
        }

        return tag;
    }

    public static NBTTagList writeUUIDtoUUIDMap(Map<UUID, UUID> map) {
        NBTTagList list = new NBTTagList();
        for (Map.Entry<UUID, UUID> tracker: map.entrySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("Player", tracker.getKey().toString());
            tag.setString("Team", tracker.getValue().toString());
            list.appendTag(tag);
        }

        return list;
    }

    public static void readUUIDtoUUIDMap(NBTTagList list, Map<UUID, UUID> map) {
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            UUID uuidPlayer = UUID.fromString(tag.getString("Player"));
            UUID uuidTeam = UUID.fromString(tag.getString("Team"));
            map.put(uuidPlayer, uuidTeam);
        }
    }

    public static NBTTagList writeObjIntMap(Object2IntMap<ResourceLocation> map) {
        NBTTagList list = new NBTTagList();
        map.forEach((key, value) -> {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("Resource", key.toString());
            tag.setInteger("Value", value);
            list.appendTag(tag);
        });


        return list;
    }

    public static void readObjIntMap(NBTTagList list, Object2IntMap<ResourceLocation> map) {
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            ResourceLocation resource = new ResourceLocation(tag.getString("Resource"));
            int value = tag.getInteger("Value");
            map.put(resource, value);
        }
    }

    public static NBTTagCompound writeHolderCollectionToTag(Collection<? extends Holder> obtained) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("list", writeHolderCollection(obtained));
        return tag;
    }

    public static NBTTagCompound writeHolderToTag(Holder holder) {
        NBTTagCompound tag = holder.serializeNBT();
        tag.setString("name", holder.name());
        return tag;
    }

    public static NBTTagList writeHolderCollection(Collection<? extends Holder> obtained) {
        NBTTagList list = new NBTTagList();
        obtained.forEach((holder) -> list.appendTag(writeHolderToTag(holder)));
        return list;
    }

    public static  <H extends Holder> Collection<H> readHolderCollectionFromTag(NBTTagCompound tag) {
        NBTTagList list = tag.getTagList("list", 10);
        Collection<H> set = Sets.newHashSet();
        readHolderCollection(list, set);
        return set;
    }

    @SuppressWarnings("unchecked")
    public static<H extends Holder> H readHolderFromTag(NBTTagCompound tag) throws IllegalAccessException, InstantiationException {
        Class<H> clazz = (Class<H>) Holder.TYPES.get(tag.getString("name")).getClass();
        H holder = clazz.newInstance();
        holder.deserializeNBT(tag);
        return holder;
    }

    @SuppressWarnings("unchecked")
    public static <H extends Holder> void readHolderCollection(NBTTagList list, Collection<H> set) {
        try {
            for (int i = 0; i < list.tagCount(); i++) {
                set.add(readHolderFromTag(list.getCompoundTagAt(i)));
            }
        } catch (IllegalAccessException | InstantiationException ignored) {}
    }

    public static NBTTagCompound getItemNBT(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        return stack.getTagCompound();
    }

    public static NBTTagCompound getOrCreateTag(NBTTagCompound tag, String name) {
        if (tag.hasKey(name)) return tag.getCompoundTag(name);
        else {
            NBTTagCompound data = new NBTTagCompound();
            tag.setTag(name, data);
            return data;
        }
    }

    public static NBTTagCompound write(WorldSavedData data) {
        NBTTagCompound tag = new NBTTagCompound();
        data.writeToNBT(tag);
        return tag;
    }

    public static <I extends INBTSerializable<NBTTagCompound>> NBTTagList serialize(Map<UUID, I> data) {
        NBTTagList list = new NBTTagList();
        data.forEach((uuid, stats) -> {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("UUID", uuid.toString());
            nbt.setTag("Data", stats.serializeNBT());
            list.appendTag(nbt);
        });

        return list;
    }

    public static <I extends INBTSerializable<NBTTagCompound>, S extends WorldSavedData> void deserialize(S save, Class<I> clazz, NBTTagList list, Map<UUID, I> data) {
        try {
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound nbt = list.getCompoundTagAt(i);
                UUID uuid = UUID.fromString(nbt.getString("UUID"));
                I stats = clazz.getConstructor(save.getClass(), UUID.class).newInstance(save, uuid);
                stats.deserializeNBT(nbt.getCompoundTag("Data"));
                data.put(uuid, stats);
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ignored) {}
    }

    public static <I extends PenguinRegistry> NBTTagList writePenguinRegistry(Collection<I> collection) {
        NBTTagList list = new NBTTagList();
        for (I i : collection) {
            list.appendTag(new NBTTagString(i.getRegistryName().toString()));
        }

        return list;
    }

    public static <I extends PenguinRegistry> void readPenguinRegistry(NBTTagList list, Map<ResourceLocation, I> map, Collection<I> collection) {
        for (int i = 0; i < list.tagCount(); i++) {
            ResourceLocation resource = new ResourceLocation(list.getStringTagAt(i));
            collection.add(map.get(resource));
        }
    }

    public static <I extends INBTSerializable<NBTTagCompound>> NBTTagList writeDimensionMap(Int2ObjectMap<I> map) {
        NBTTagList list = new NBTTagList();
        map.forEach((id, data) -> {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("ID", id);
            nbt.setTag("Data", data.serializeNBT());
            list.appendTag(nbt);
        });

        return list;
    }

    public static <I extends INBTSerializable<NBTTagCompound>> void readDimensionMap(Int2ObjectMap<I> map, NBTTagList list, Class<I> clazz) {
        try {
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound nbt = list.getCompoundTagAt(i);
                int id = nbt.getInteger("ID");
                I data = clazz.newInstance();
                data.deserializeNBT(nbt.getCompoundTag("Data"));
                map.put(id, data);
            }
        } catch (InstantiationException | IllegalAccessException ignored) {}
    }
}
