package uk.joshiejack.settlements.npcs.gifts;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Objects;

public class GiftCategory {
    public static final Map<String, GiftCategory> REGISTRY = Maps.newHashMap();
    private final String name;
    private final GiftQuality quality;

    GiftCategory(String name, GiftQuality quality) {
        this.name = name;
        this.quality = quality;
        REGISTRY.put(name, this);
    }

    public String name() {
        return name;
    }

    public GiftQuality quality() {
        return quality;
    }

    public static GiftCategory get(String clazz) {
        return REGISTRY.get(clazz);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftCategory giftClass = (GiftCategory) o;
        return Objects.equals(name, giftClass.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
