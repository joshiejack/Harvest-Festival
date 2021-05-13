package uk.joshiejack.economy.shop.condition;

import uk.joshiejack.economy.api.shops.Condition;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import javax.annotation.Nonnull;

@PenguinLoader("named")
public class ConditionName extends Condition {
    private String name;

    @Override
    public Condition create(Row data, String id) {
        ConditionName validator = new ConditionName();
        validator.name = data.name();
        return validator;
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        return target.input.getName(target).equals(name);
    }
}
