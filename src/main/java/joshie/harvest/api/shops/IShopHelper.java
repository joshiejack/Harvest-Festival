package joshie.harvest.api.shops;

import net.minecraft.item.ItemStack;

public interface IShopHelper {
    /** Creates a default opening handler **/
    OpeningHandler createDefaultOpeningHandler();

    IPurchasable createDefaultPurchasable(long cost, ItemStack stack);
    IPurchasable createDefaultPurchasableWithLimitedStock(long cost, ItemStack stack, int stock);
}
