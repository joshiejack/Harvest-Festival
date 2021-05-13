package uk.joshiejack.seasons.shop.condition;

import uk.joshiejack.economy.api.shops.Condition;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.seasons.data.database.SeasonDataLoader;
import uk.joshiejack.seasons.handlers.SeasonHandler;
import uk.joshiejack.seasons.world.SeasonsWorldProvider;

import javax.annotation.Nonnull;

@PenguinLoader("season_handler")
public class ConditionSeasonHandler extends Condition {
    private SeasonHandler handler;

    public ConditionSeasonHandler() {}

    @Override
    public Condition create(Row data, String id) {
        ConditionSeasonHandler validator = new ConditionSeasonHandler();
        validator.handler = SeasonDataLoader.SEASON_HANDLERS.get(data.get("season_handler_id").toString());
        return validator;
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        return type == CheckType.SHOP_EXISTS || handler.getSeasons().contains(SeasonsWorldProvider.getWorldData(target.world).getSeason());
    }
}
