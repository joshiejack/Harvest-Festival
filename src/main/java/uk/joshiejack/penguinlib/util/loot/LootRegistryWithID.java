package uk.joshiejack.penguinlib.util.loot;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Random;

public class LootRegistryWithID<I> extends LootRegistry<I> {
    private final Int2ObjectMap<I> byID = new Int2ObjectOpenHashMap<>();

    public void add(int id, I value, double weight) {
        this.byID.put(id, value);
        this.add(value, weight);
    }

    @Override
    @Nonnull
    public I get(Random rand) {
        return Objects.requireNonNull(super.get(rand));
    }

    public I byID(int id) {
        if (id >= byID.size() || byID.size() < 0) return byID.get(0);
        else return byID.get(id);
    }

    public boolean isSingleEntry() {
        return byID.size() == 1;
    }
}
