package uk.joshiejack.economy.shop.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import uk.joshiejack.economy.api.shops.ListingHandler;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.economy.shop.Department;
import uk.joshiejack.economy.shop.input.ShopInputEntity;
import uk.joshiejack.penguinlib.data.database.Database;
import uk.joshiejack.penguinlib.util.PenguinLoader;

@SuppressWarnings("ConstantConditions")
@PenguinLoader("shop")
public class ShopListingHandler extends ListingHandler<String> {
    @Override
    public String getType() {
        return "shop";
    }

    @Override
    public String getObjectFromDatabase(Database database, String data) {
        return data;
    }

    @Override
    public String getDisplayName(String shop) {
        return Department.REGISTRY.get(shop).getLocalizedName();
    }

    @Override
    public ItemStack[] createIcon(String shop) {
        return new ItemStack[] { Department.REGISTRY.get(shop).getIcon() };
    }

    @Override
    public void purchase(EntityPlayer player, String id) {
        if (!player.world.isRemote) {
            Department shop = Department.REGISTRY.get(id);
            ShopTarget target = new ShopTarget(player.world, new BlockPos(player), player, player, player.getHeldItemMainhand(), new ShopInputEntity(player));
            if (shop != null) {
                shop.open(target);
            }
        }
    }

    @Override
    public String getStringFromObject(String shop) {
        return shop.toString();
    }

    @Override
    public boolean isValid(String shop) {
        return shop != null && !shop.isEmpty();
    }

    @Override
    public String getValidityError() {
        return "Shop does not exist";
    }
}

