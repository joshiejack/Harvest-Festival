package uk.joshiejack.economy.shop.condition;

import uk.joshiejack.economy.api.shops.Condition;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import javax.annotation.Nonnull;

@PenguinLoader("has_tag")
public class ConditionData extends Condition {
    private String key;
    private String value;

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        return type != CheckType.SHOP_LISTING && target.input.hasTag(target, key, value);
    }

    @Override
    public Condition create(Row data, String id) {
        ConditionData validator = new ConditionData();
        validator.key = data.get("tag_name");
        validator.value = data.get("tag_data");
        return validator;
    }
}
