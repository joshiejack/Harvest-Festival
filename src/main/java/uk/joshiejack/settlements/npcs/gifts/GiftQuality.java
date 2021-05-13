package uk.joshiejack.settlements.npcs.gifts;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Objects;

public class GiftQuality {
    public static final Map<String, GiftQuality> REGISTRY = Maps.newHashMap();
    private final String name;
    private final int value;

    GiftQuality(String name, int value) {
        this.name = name;
        this.value = value;
        REGISTRY.put(name, this);
    }

    public static GiftQuality get(String quality) {
        return REGISTRY.get(quality);
    }

    public String name() {
        return name;
    }

    public int value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftQuality that = (GiftQuality) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
