package uk.joshiejack.economy.shop.comparator;

import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import javax.annotation.Nonnull;

@PenguinLoader("redstone_level")
public class ComparatorRedstone extends ComparatorImmutable {
    @Override
    public int getValue(@Nonnull ShopTarget target) {
        return target.world.getRedstonePowerFromNeighbors(target.pos);
    }
}
