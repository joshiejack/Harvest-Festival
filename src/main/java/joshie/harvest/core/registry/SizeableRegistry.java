package joshie.harvest.core.registry;

import joshie.harvest.api.core.ISizeableRegistry;
import joshie.harvest.api.core.ISizedProvider;
import joshie.harvest.api.core.Size;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import joshie.harvest.core.util.holders.ItemStackHolder;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

@HFApiImplementation
@SuppressWarnings("unused")
public class SizeableRegistry implements ISizeableRegistry {
    public static final SizeableRegistry INSTANCE = new SizeableRegistry();
    private final HashMap<ItemStackHolder, Size> providers = new HashMap<>();

    private SizeableRegistry() {}


    @Override
    public void registerStackAsSize(ItemStack stack, Size size) {
        providers.put(ItemStackHolder.of(stack.getItem(), stack.getItemDamage()), size);
    }

    @Override
    public Size getSize(ItemStack stack) {
        if (stack.getItem() instanceof ISizedProvider) {
            return ((ISizedProvider)stack.getItem()).getSize(stack);
        }

        return providers.get(ItemStackHolder.of(stack));
    }
}
