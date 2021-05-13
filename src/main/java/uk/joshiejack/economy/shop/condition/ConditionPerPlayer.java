package uk.joshiejack.economy.shop.condition;

import uk.joshiejack.economy.api.shops.Condition;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.economy.shop.Department;
import uk.joshiejack.economy.shop.Listing;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

import static uk.joshiejack.economy.Economy.MODID;

@PenguinLoader("per_player")
public class ConditionPerPlayer extends Condition {
    private int max;

    public ConditionPerPlayer() {}
    private ConditionPerPlayer(int max) {
        this.max = max;
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        return type == CheckType.SHOP_LISTING && super.valid(target, type);
    }

    @Override
    public Condition create(Row data, String id) {
        return new ConditionPerPlayer(data.get("max"));
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull Department department, @Nonnull Listing listing, @Nonnull CheckType type) {
        if (type != CheckType.SHOP_LISTING) return false;
        else {
            NBTTagCompound tag = target.player.getEntityData().getCompoundTag(MODID);
            String label = department.id() + ":" + listing.getID();
            return tag.getInteger(label) < max;
        }
    }

    @Override
    public void onPurchase(EntityPlayer player, @Nonnull Department department, @Nonnull Listing listing) {
        NBTTagCompound tag = player.getEntityData().getCompoundTag(MODID);
        String label = department.id() + ":" + listing.getID();
        tag.setInteger(label, tag.getInteger(label) + 1);
    }
}
