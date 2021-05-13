package uk.joshiejack.penguinlib.util.helpers.generic;

import it.unimi.dsi.fastutil.objects.Object2IntMap;

public class MapHelper {
    public static <T> int adjustOrPut(Object2IntMap<T> map, T key, int adjust, int put) {
        if (!map.containsKey(key)) {
            map.put(key, put);
            return put;
        } else {
            int original = map.getInt(key) + adjust;
            map.put(key, original);
            return original;
        }
    }

    public static <T> void increment(Object2IntMap<T> map, T key) {
        map.put(key, map.getInt(key) + 1);
    }

    public static <T> void adjustValue(Object2IntMap<T> map, T key, int amount) {
        map.put(key, map.getInt(key) + amount);
    }
}
