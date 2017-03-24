package joshie.harvest.shops.purchasable;

import joshie.harvest.api.HFApi;
import net.minecraft.item.ItemStack;

public class PurchasableStock extends Purchasable {
    public PurchasableStock(ItemStack stack) {
        super((long) -(HFApi.shipping.getSellValue(stack) * 1.25), stack);
    }
}
