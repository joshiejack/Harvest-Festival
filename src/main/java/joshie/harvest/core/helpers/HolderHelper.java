package joshie.harvest.core.helpers;

import joshie.harvest.core.util.holders.AbstractDataHolder;

import java.util.Collection;

public class HolderHelper {
    @SuppressWarnings("unchecked")
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
}
