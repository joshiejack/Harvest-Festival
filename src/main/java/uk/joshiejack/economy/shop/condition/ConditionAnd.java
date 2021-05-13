package uk.joshiejack.economy.shop.condition;

import uk.joshiejack.economy.api.shops.Condition;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import javax.annotation.Nonnull;

@PenguinLoader("and")
public class ConditionAnd extends ConditionList {
    @Override
    protected String getTableName() {
        return "condition_and";
    }

    @Override
    protected String getFieldName() {
        return "and_condition";
    }

    @Override
    public Condition create(Row database, String id) {
        return new ConditionAnd();
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        return conditions.stream().allMatch(condition -> condition.valid(target, type));
    }
}
