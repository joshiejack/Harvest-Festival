package joshie.harvest.shops.purchasable;

import joshie.harvest.api.crops.Crop;
import net.minecraft.item.ItemStack;

public class PurchasableCrop extends Purchasable {
    private final Crop crop;

    public PurchasableCrop(Crop crop) {
        this.crop = crop;
        this.cost = 20 + crop.getSellValue() * 3;
        this.resource = ((cost >= 0) ? "buy:" : "sell:") + crop.getResource().toString().replace(":", "_");
    }

    @Override
    public ItemStack getDisplayStack() {
        if (stack == null) {
            stack = crop.getCropStack(1);
        }

        return stack;
    }
}
