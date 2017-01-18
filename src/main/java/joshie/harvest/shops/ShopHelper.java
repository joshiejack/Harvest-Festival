package joshie.harvest.shops;

import joshie.harvest.api.shops.IPurchasable;
import joshie.harvest.api.shops.IShopHelper;
import joshie.harvest.api.shops.OpeningHandler;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import joshie.harvest.shops.purchasable.Purchasable;
import net.minecraft.item.ItemStack;

@HFApiImplementation
@SuppressWarnings("unused")
public class ShopHelper implements IShopHelper {
    public static final ShopHelper INSTANCE = new ShopHelper();

    @Override
    public OpeningHandler createDefaultOpeningHandler() {
        return new ShopHours();
    }

    @Override
    public IPurchasable createDefaultPurchasable(long cost, ItemStack stack) {
        return new Purchasable(cost, stack);
    }

    @Override
    public IPurchasable createDefaultPurchasableWithLimitedStock(long cost, ItemStack stack, int stock) {
        return new Purchasable(cost, stack).setStock(stock);
    }
}
