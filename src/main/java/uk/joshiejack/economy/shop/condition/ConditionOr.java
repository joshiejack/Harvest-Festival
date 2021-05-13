package uk.joshiejack.economy.shop.condition;

import uk.joshiejack.economy.api.shops.Condition;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import javax.annotation.Nonnull;

@PenguinLoader("or")
public class ConditionOr extends ConditionList {
    @Override
    protected String getTableName() {
        return "condition_or";
    }

    @Override
    protected String getFieldName() {
        return "or_condition";
    }

    @Deprecated
    @Override
    public Condition create(Row database, String id) {
        return new ConditionOr();
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        return conditions.stream().anyMatch(condition -> condition.valid(target, type));
    }
}
