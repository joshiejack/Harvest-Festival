package uk.joshiejack.settlements.shop.comparator;

import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.npcs.status.Statuses;
import uk.joshiejack.economy.api.shops.Comparator;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import javax.annotation.Nonnull;

@PenguinLoader("total_npc_status")
public class ComparatorTotalNPCStatus extends Comparator {
    private String status;

    @Override
    public Comparator create(Row data, String id) {
        ComparatorTotalNPCStatus comparator = new ComparatorTotalNPCStatus();
        comparator.status = data.get("status");
        return comparator;
    }

    public int getValue(@Nonnull ShopTarget target) {
        if (target.world.isRemote) {
            return Statuses.total(status);
        } else return AdventureDataLoader.get(target.world).getStatusTracker(target.player).total(status);
    }
}
