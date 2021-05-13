package uk.joshiejack.settlements.shop.comparator;

import uk.joshiejack.settlements.util.TownFinder;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.economy.shop.comparator.ComparatorImmutable;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import javax.annotation.Nonnull;

@PenguinLoader("town_population")
public class ComparatorTownPopulation extends ComparatorImmutable {
    @Override
    public int getValue(@Nonnull ShopTarget target) {
        return TownFinder.find(target.world, target.pos).getCensus().population();
    }
}
