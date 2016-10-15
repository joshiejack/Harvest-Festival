package joshie.harvest.core.registry;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.IShippable;
import joshie.harvest.api.core.IShippingRegistry;
import joshie.harvest.api.core.Ore;
import joshie.harvest.api.crops.Crop;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import joshie.harvest.core.util.holders.HolderRegistry;
import net.minecraft.item.ItemStack;

@HFApiImplementation
public class ShippingRegistry implements IShippingRegistry {
    public static final ShippingRegistry INSTANCE = new ShippingRegistry();
    private final HolderRegistry<Long> registry = new HolderRegistry<>();

    private ShippingRegistry() {}

    @Override
    public void registerSellable(ItemStack stack, long value) {
        registry.register(stack, value);
    }

    @Override
    public void registerSellable(Ore ore, long value) {
        registry.register(ore, value);
    }

    @Override
    public long getSellValue(ItemStack stack) {
        if (stack.getItem() instanceof IShippable) {
            return ((IShippable)stack.getItem()).getSellValue(stack);
        }

        //Special case Crops
        Crop crop = HFApi.crops.getCropFromStack(stack);
        if (crop != null) {
            return crop.getSellValue(stack);
        }

        Long value = registry.getValueOf(stack);
        return value == null ? 0L : value;
    }
}
