package joshie.harvest.core.util.holders;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import joshie.harvest.api.core.ISizedProvider;
import joshie.harvest.api.crops.ICropProvider;
import joshie.harvest.crops.Crop;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collection;
import java.util.HashMap;

public class HolderRegistry<R> {
    private final HashMap<AbstractItemHolder, R> registry = new HashMap<>();
    private final Multimap<Item, AbstractItemHolder> keyMap = HashMultimap.create();

    public void registerItem(ItemStack stack, R r) {
        AbstractItemHolder holder = getHolder(stack);
        keyMap.get(stack.getItem()).add(holder); //Link the item to various holders
        registry.put(holder, r); //Link the holder to the actual item
    }

    public void registerMod(String mod, R r) {
        ModHolder holder = ModHolder.of(mod);
        //Loop through every item in the registry, and associate it with this map
        for (Item item: Item.REGISTRY) {
            if (item.getRegistryName().getResourceDomain().equals(mod)) {
                keyMap.get(item).add(holder);
            }
        }

        registry.put(holder, r);
    }

    public boolean matches(ItemStack stack, R type) {
        Collection<AbstractItemHolder> holders = keyMap.get(stack.getItem());
        for (AbstractItemHolder holder: holders) {
            if (holder.matches(stack) && registry.get(holder) == type) {
                return true;
            }
        }

        return false;
    }

    public boolean matches(R external, R internal) {
        return external == internal;
    }

    public static AbstractItemHolder getHolder(ItemStack stack) {
        if (stack.getItem() instanceof ICropProvider) return CropHolder.of((Crop)((ICropProvider)stack.getItem()).getCrop(stack));
        else if (stack.getItem() instanceof ISizedProvider) return SizeableHolder.of(((ISizedProvider)stack.getItem()).getSizeable(stack));
        else if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE) return ItemHolder.of(stack.getItem());
        else return ItemStackHolder.of(stack);
    }
}
