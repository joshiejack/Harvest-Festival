package uk.joshiejack.settlements.shop.comparator;

import uk.joshiejack.settlements.util.TownFinder;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.economy.shop.comparator.ComparatorImmutable;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import javax.annotation.Nonnull;

@PenguinLoader("town_age")
public class ComparatorTownAge extends ComparatorImmutable {
    @Override
    public int getValue(@Nonnull ShopTarget target) {
        Town<?> town = TownFinder.find(target.world, target.pos);
        return town.getCharter().getAge(target.world.getWorldTime());
    }
}
