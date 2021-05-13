package uk.joshiejack.economy.shop.comparator;

import uk.joshiejack.economy.api.shops.Comparator;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.InventoryHelper;

import javax.annotation.Nonnull;

@PenguinLoader("in_inventory")
public class ComparatorInInventory extends Comparator {
    private Holder holder;

    @Override
    public Comparator create(Row data, String id) {
        ComparatorInInventory comparator = new ComparatorInInventory();
        comparator.holder = data.holder();
        return comparator;
    }

    @Override
    public int getValue(@Nonnull ShopTarget target) {
        return InventoryHelper.getCount(target.player, holder);
    }
}
