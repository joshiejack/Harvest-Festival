package uk.joshiejack.settlements.shop.comparator;

import uk.joshiejack.settlements.util.TownFinder;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.economy.shop.comparator.ComparatorImmutable;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import javax.annotation.Nonnull;

@PenguinLoader("total_unique_building_count")
public class ComparatorTotalUniqueBuildings extends ComparatorImmutable {
    @Override
    public int getValue(@Nonnull ShopTarget target) {
        return TownFinder.find(target.world, target.pos).getLandRegistry().uniqueBuildingsCount();
    }
}
