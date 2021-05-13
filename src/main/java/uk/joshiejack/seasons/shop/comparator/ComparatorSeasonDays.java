package uk.joshiejack.seasons.shop.comparator;

import uk.joshiejack.economy.api.shops.Comparator;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.seasons.Seasons;
import uk.joshiejack.seasons.SeasonsConfig;

import javax.annotation.Nonnull;

@PenguinLoader("season_days")
public class ComparatorSeasonDays extends Comparator {
    private int seasons;

    @Override
    public Comparator create(Row data, String id) {
        ComparatorSeasonDays comparator = new ComparatorSeasonDays();
        comparator.seasons = data.get("days");
        return comparator;
    }

    @Override
    public int getValue(@Nonnull ShopTarget target) {
        return seasons * (SeasonsConfig.daysPerSeasonMultiplier * Seasons.DAYS_PER_SEASON);
    }
}
