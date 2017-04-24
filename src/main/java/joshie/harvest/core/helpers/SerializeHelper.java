package joshie.harvest.core.helpers;

import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.util.adapter.CalendarAdapter;
import joshie.harvest.core.util.adapter.QuestAdapter;
import joshie.harvest.core.util.adapter.SerializeAdapter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class SerializeHelper {
    private static final HashMap<Class, SerializeAdapter> ADAPTERS = new HashMap<>();
    static {
        ADAPTERS.put(Quest.class, new QuestAdapter());
        ADAPTERS.put(CalendarDate.class, new CalendarAdapter());
    }

    @SuppressWarnings("unchecked")
    public static <K, V>void writeMap(Map<K, V> map, String name, NBTTagCompound tag) {
        NBTTagList list = new NBTTagList();
        for (Entry<K, V> entry: map.entrySet()) {
            if (entry == null) continue;
            NBTTagCompound nbt = new NBTTagCompound();
            ADAPTERS.get(Quest.class).writeToNBT(entry.getKey(), nbt);
            ADAPTERS.get(CalendarDate.class).writeToNBT(entry.getValue(), nbt);
            list.appendTag(nbt);
        }

        tag.setTag(name, list);
    }


    public static <K, V> Map<K, V> readMap(Class<K> key, Class<V> value, String name, NBTTagCompound tag) {
        return readMap(HashMap.class, key, value, name, tag);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> readMap(Class<? extends Map> clazz, Class<K> key, Class<V> value, String name, NBTTagCompound tag) {
        try {
            Map<K, V> map = clazz.newInstance();
            NBTTagList list = tag.getTagList(name, 10);
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound nbt = list.getCompoundTagAt(i);
                map.put((K)ADAPTERS.get(key).readFromNBT(nbt), (V)ADAPTERS.get(value).readFromNBT(nbt));
            }

            return map;
        } catch (InstantiationException | IllegalAccessException ex) { return null; }
    }
}
