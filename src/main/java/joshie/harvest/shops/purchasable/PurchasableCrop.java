package joshie.harvest.shops.purchasable;

import joshie.harvest.api.crops.Crop;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

public class PurchasableCrop extends PurchasableFML<Crop> {
    private ItemStack stack;

    public PurchasableCrop(Crop crop) {
        super(0, crop.getRegistryName());
        cost = 20 + item.getSellValue() * 3;
    }

    @Override
    public IForgeRegistry<Crop> getRegistry() {
        return Crop.REGISTRY;
    }

    @Override
    public ItemStack getDisplayStack() {
        if (stack == null) {
            stack = item.getCropStack(1);
        }

        return stack;
    }
}
