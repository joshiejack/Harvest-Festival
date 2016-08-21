package joshie.harvest.core.helpers.generic;

import joshie.harvest.core.util.holders.AbstractDataHolder;

import java.util.Collection;
import java.util.Map;

public class CollectionHelper {
    public static <T extends AbstractDataHolder> T mergeCollection(T t, Collection<T> collection) {
        if (!collection.contains(t)) {
            collection.add(t); //Just add the collection
            return t;
        } else {
            for (T c: collection) {
                if (c.equals(t)) {
                    c.merge(t); //merge
                    return c;
                }
            }

            return null;
        }
    }

    public static <K, V extends AbstractDataHolder> void mergeMap(K key, V value, Map<K, V> map) {
        V existing = map.get(key);
        if (existing == null) {
            map.put(key, value);
        } else {
            existing.merge(value);
        }
    }
}
