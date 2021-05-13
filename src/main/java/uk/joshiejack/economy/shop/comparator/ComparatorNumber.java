package uk.joshiejack.economy.shop.comparator;

import uk.joshiejack.economy.api.shops.Comparator;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import javax.annotation.Nonnull;

@PenguinLoader("number")
public class ComparatorNumber extends Comparator {
    private int value;

    @Override
    public Comparator create(Row data, String id) {
        ComparatorNumber comparator = new ComparatorNumber();
        comparator.value = data.get("number");
        return comparator;
    }

    @Override
    public int getValue(@Nonnull ShopTarget target) {
        return this.value;
    }
}
