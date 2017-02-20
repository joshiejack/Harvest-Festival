package joshie.harvest.core.registry;

import joshie.harvest.api.core.IShippingRegistry;
import joshie.harvest.api.core.Ore;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import joshie.harvest.core.util.holders.HolderRegistry;
import net.minecraft.item.ItemStack;

@HFApiImplementation
public class ShippingRegistry implements IShippingRegistry {
    public static final ShippingRegistry INSTANCE = new ShippingRegistry();
    public static final String SELL_VALUE = "SellValue";
    private final HolderRegistry<Long> registry = new HolderRegistry<>();

    private ShippingRegistry() {}

    public HolderRegistry<Long> getRegistry() {
        return registry;
    }

    @Override
    public void registerSellable(ItemStack stack, long value) {
        registry.register(stack, value);
    }

    @Override
    public void registerSellable(Ore ore, long value) {
        registry.register(ore, value);
    }

    @Override
    @SuppressWarnings("ConstantConditions, deprecation")
    public long getSellValue(ItemStack stack) {
        //Per item override
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey(SELL_VALUE)) {
            return stack.getTagCompound().getLong(SELL_VALUE);
        }

        //Return the registry value first, so we can override
        Long value = registry.getValueOf(stack);
        return value != null ? value : 0L;
    }
}
