package uk.joshiejack.economy.shop.builder;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public abstract class ListingBuilder <I> {
    public static final Map<String, ListingBuilder> BUILDERS = Maps.newHashMap();

    public abstract List<String> items();
}
