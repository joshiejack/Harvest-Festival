package joshie.harvest.core.registry;

import joshie.harvest.api.core.IShippable;
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

    @Override
    public void registerSellable(ItemStack stack, long value) {
        registry.register(stack, value);
    }

    @Override
    public void registerSellable(Ore ore, long value) {
        registry.register(ore, value);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public long getSellValue(ItemStack stack) {
        //Per item override
        if (stack.hasTagCompound()) {
            return stack.getTagCompound().getLong(SELL_VALUE);
        }

        //Return the registry value first, so we can override
        Long value = registry.getValueOf(stack);
        if (value != null) return value;

        //Shippables
        //TODO: Remove in 0.7+
        if (stack.getItem() instanceof IShippable) {
            return ((IShippable)stack.getItem()).getSellValue(stack);
        }

        return 0L;
    }
}
