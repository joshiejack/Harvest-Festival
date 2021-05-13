package uk.joshiejack.husbandry.animals.traits;

import com.google.common.collect.Maps;

import java.util.Map;

public abstract class AnimalTrait {
    public static final Map<String, AnimalTrait> TRAITS = Maps.newHashMap();
    private final String name;
    public AnimalTrait(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
