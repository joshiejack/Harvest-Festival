package uk.joshiejack.economy.shop.condition;

import uk.joshiejack.economy.api.shops.Condition;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

@PenguinLoader("script")
public class ConditionScript extends Condition {
    private ResourceLocation script;

    @Override
    public Condition create(Row data, String id) {
        ConditionScript validator = new ConditionScript();
        validator.script = data.getScript();
        return validator;
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        return Scripting.get(script).isTrue("isValid", target.world, target.pos, target.entity, target.player, target.stack, target.input);
    }
}
