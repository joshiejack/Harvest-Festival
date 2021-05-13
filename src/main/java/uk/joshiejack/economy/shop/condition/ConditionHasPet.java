package uk.joshiejack.economy.shop.condition;

import uk.joshiejack.economy.api.shops.Condition;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.economy.shop.Department;
import uk.joshiejack.economy.shop.Listing;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

@PenguinLoader("has_pet")
public class ConditionHasPet extends Condition {
    private ResourceLocation type;

    public ConditionHasPet() {}
    public ConditionHasPet(String string) {
        this.type = new ResourceLocation(string);
    }

    @Override
    public Condition create(Row data, String id) {
        return new ConditionHasPet(data.get("entity"));
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull Department department, @Nonnull Listing listing, @Nonnull CheckType type) {
        return target.world.getLoadedEntityList().stream()
                .filter(entity -> entity instanceof IEntityOwnable && EntityList.getKey(entity).equals(this.type))
                .anyMatch(e -> ((IEntityOwnable) e).getOwnerId().equals(PlayerHelper.getUUIDForPlayer(target.player)));
    }
}
