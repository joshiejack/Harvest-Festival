package uk.joshiejack.economy.shop.comparator;

import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import javax.annotation.Nonnull;

@PenguinLoader("light_level")
public class ComparatorLightLevel extends ComparatorImmutable {
    @Override
    public int getValue(@Nonnull ShopTarget target) {
        return target.world.getLightFromNeighbors(target.pos);
    }
}
