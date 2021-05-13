package uk.joshiejack.economy.shop.comparator;

import uk.joshiejack.economy.api.shops.Comparator;
import uk.joshiejack.penguinlib.data.database.Row;

public abstract class ComparatorImmutable extends Comparator {
    @Override
    public Comparator create(Row row, String id) {
        return this;
    }
}
