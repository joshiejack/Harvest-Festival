package uk.joshiejack.economy.shop.comparator;

import uk.joshiejack.economy.api.shops.Comparator;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import javax.annotation.Nonnull;

@PenguinLoader("player_status")
public class ComparatorPlayerStatus extends Comparator {
    private String status;

    @Override
    public Comparator create(Row data, String id) {
        ComparatorPlayerStatus comparator = new ComparatorPlayerStatus();
        comparator.status = data.get("status");
        return comparator;
    }

    @Override
    public int getValue(@Nonnull ShopTarget target) {
        return target.player.getEntityData().getCompoundTag("PenguinStatuses").getInteger(status);
    }
}
