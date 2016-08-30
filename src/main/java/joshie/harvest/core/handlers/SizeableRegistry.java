package joshie.harvest.core.handlers;

import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.api.core.ISizeableRegistry;
import joshie.harvest.api.core.ISizedProvider;
import joshie.harvest.core.lib.Sizeable;
import joshie.harvest.core.util.HFApiImplementation;
import joshie.harvest.core.util.holder.ItemStackHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.RegistryBuilder;

import java.util.HashMap;

import static joshie.harvest.core.lib.HFModInfo.MODID;

@HFApiImplementation
public class SizeableRegistry implements ISizeableRegistry {
    public static final IForgeRegistry<Sizeable> REGISTRY = new RegistryBuilder<Sizeable>().setName(new ResourceLocation("harvestfestival", "sizeables")).setType(Sizeable.class).setIDRange(0, 32000).create();
    public static final SizeableRegistry INSTANCE = new SizeableRegistry();
    private final HashMap<ItemStackHolder, Size> providers = new HashMap<>();

    private SizeableRegistry() {}

    public Sizeable registerSizeable(String name, long sellSmall, long sellMedium, long sellLarge) {
        Sizeable meta = new Sizeable(name, sellSmall, sellMedium, sellLarge);
        meta.setRegistryName(new ResourceLocation(MODID, name));
        REGISTRY.register(meta);
        return meta;
    }

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
