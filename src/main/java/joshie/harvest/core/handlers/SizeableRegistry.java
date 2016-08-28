package joshie.harvest.core.handlers;

import joshie.harvest.api.core.ISizeable;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.api.core.ISizeableRegistry;
import joshie.harvest.api.core.ISizedProvider;
import joshie.harvest.core.lib.Sizeable;
import joshie.harvest.core.base.item.ItemSizeable;
import joshie.harvest.core.util.HFApiImplementation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.PersistentRegistryManager;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;

import static joshie.harvest.core.lib.HFModInfo.MODID;

@HFApiImplementation
public class SizeableRegistry implements ISizeableRegistry {
    public static final FMLControlledNamespacedRegistry<Sizeable> REGISTRY = PersistentRegistryManager.createRegistry(new ResourceLocation(MODID, "sizeables"), Sizeable.class, null, 0, 32000, true, null, null, null);
    public static final SizeableRegistry INSTANCE = new SizeableRegistry();
    private final HashMap<Pair<Item, Integer>, Pair<ISizeable, Size>> providers = new HashMap<>();

    private SizeableRegistry() {}

    @Override
    public Item createSizedItem(String name, long sellSmall, long sellMedium, long sellLarge) {
        Sizeable meta = (Sizeable) registerSizeable(new ResourceLocation(MODID, name), sellSmall, sellMedium, sellLarge);
        return new ItemSizeable(name, meta).register(name.toLowerCase());
    }

    @Override
    public ISizeable registerSizeable(ResourceLocation key, long sellSmall, long sellMedium, long sellLarge) {
        Sizeable meta = new Sizeable(sellSmall, sellMedium, sellLarge);
        meta.setRegistryName(key);
        REGISTRY.register(meta);
        return meta;
    }

    @Override
    public ISizeable registerSizeableProvider(ItemStack stack, ISizeable sizeable, Size size) {
        providers.put(Pair.of(stack.getItem(), stack.getItemDamage()), Pair.of(sizeable, size));
        return sizeable;
    }

    @Override
    public Pair<ISizeable, Size> getSizeableFromStack(ItemStack stack) {
        if (stack.getItem() instanceof ISizedProvider) {
            ISizeable sizeable = ((ISizedProvider)stack.getItem()).getSizeable(stack);
            Size size = ((ISizedProvider)stack.getItem()).getSize(stack);
            return Pair.of(sizeable, size);
        }

        Pair<ISizeable, Size> sizeable = providers.get(Pair.of(stack.getItem(), OreDictionary.WILDCARD_VALUE));
        return sizeable != null ? sizeable : providers.get(Pair.of(stack.getItem(), stack.getItemDamage()));
    }

    private Pair<ISizeable, Size> getPair(ItemStack stack) {
        Pair<ISizeable, Size> sizeable = providers.get(Pair.of(stack.getItem(), OreDictionary.WILDCARD_VALUE));
        return sizeable != null ? sizeable : providers.get(Pair.of(stack.getItem(), stack.getItemDamage()));
    }

    @Override
    public ISizeable getSizeable(ItemStack stack) {
        if (stack.getItem() instanceof ISizedProvider) {
            return ((ISizedProvider)stack.getItem()).getSizeable(stack);
        }

        Pair<ISizeable, Size> sizeable = getSizeableFromStack(stack);
        return sizeable != null ? sizeable.getKey() : null;
    }

    @Override
    public Size getSize(ItemStack stack) {
        if (stack.getItem() instanceof ISizedProvider) {
            return ((ISizedProvider)stack.getItem()).getSize(stack);
        }

        Pair<ISizeable, Size> sizeable = getSizeableFromStack(stack);
        return sizeable != null ? sizeable.getValue() : null;
    }


}
