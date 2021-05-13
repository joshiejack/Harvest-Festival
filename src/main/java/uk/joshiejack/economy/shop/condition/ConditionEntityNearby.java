package uk.joshiejack.economy.shop.condition;

import uk.joshiejack.economy.api.shops.Condition;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.economy.shop.Department;
import uk.joshiejack.economy.shop.Listing;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.EntityHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

@PenguinLoader("entity_nearby")
public class ConditionEntityNearby extends Condition {
    private ResourceLocation type;
    private double range;

    public ConditionEntityNearby() {}
    public ConditionEntityNearby(String string, double range) {
        this.type = new ResourceLocation(string);
        this.range = range;
    }
    
    @Override
    public Condition create(Row data, String id) {
        return new ConditionEntityNearby(data.get("entity"), data.getAsDouble("range"));
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull Department department, @Nonnull Listing listing, @Nonnull CheckType type) {
        return EntityHelper.getEntities(EntityList.getClass(this.type), target.world, target.pos, range, range).size() > 0;
    }
}
