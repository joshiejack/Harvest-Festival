package uk.joshiejack.economy.api.shops;

import com.google.common.collect.Maps;
import uk.joshiejack.penguinlib.data.database.Row;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public abstract class Comparator {
    private static final Map<String, Comparator> REGISTRY = Maps.newHashMap();
    public static final Map<String, Comparator> cache = Maps.newHashMap(); //Cleared out when finished

    public static void register(String string, Comparator comparator) {
        REGISTRY.put(string, comparator);
    }

    public static Set<String> types() {
        return REGISTRY.keySet();
    }

    @Nullable
    public static Comparator get(String s) {
        if (REGISTRY.containsKey(s)) {
            return REGISTRY.get(s);
        }

        return null;
    }

    public void merge(Row data) {}

    public static Comparator create(String id, String type, Row row) {
        return Comparator.get(type).create(row, id);
    }

    public abstract Comparator create(Row row, String id);
    public abstract int getValue(@Nonnull ShopTarget target);
}
