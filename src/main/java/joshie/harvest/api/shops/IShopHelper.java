package joshie.harvest.api.shops;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface IShopHelper {
    /** Creates a default opening handler **/
    OpeningHandler createDefaultOpeningHandler();

    IPurchasable createDefaultPurchasable(long cost, @Nonnull ItemStack stack);
    IPurchasable createDefaultPurchasableWithLimitedStock(long cost, @Nonnull ItemStack stack, int stock);
}