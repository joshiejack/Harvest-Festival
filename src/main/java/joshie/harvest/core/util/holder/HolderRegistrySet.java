package joshie.harvest.core.util.holder;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import joshie.harvest.api.crops.ICropProvider;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.base.item.ItemHFFML;
import joshie.harvest.core.item.ItemSizeable;
import joshie.harvest.crops.Crop;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collection;

public class HolderRegistrySet {
    private final Multimap<Item, AbstractItemHolder> setMap = HashMultimap.create();

    public void removeItem(Item item) {
        setMap.removeAll(item);
    }

    public void registerItem(ItemStack stack) {
        AbstractItemHolder holder = getHolder(stack);
        setMap.get(stack.getItem()).add(holder); //Link the item to various holders
    }

    public void registerMod(String mod) {
        ModHolder holder = ModHolder.of(mod);
        //Loop through every item in the registry, and associate it with this map
        for (Item item: Item.REGISTRY) {
            if (item.getRegistryName().getResourceDomain().equals(mod)) {
                setMap.get(item).add(holder);
            }
        }
    }

    public boolean contains(ItemStack stack) {
        Collection<AbstractItemHolder> holders = setMap.get(stack.getItem());
        for (AbstractItemHolder holder: holders) {
            if (holder.matches(stack)) {
                return true;
            }
        }

        return false;
    }

    private AbstractItemHolder getHolder(ItemStack stack) {
        if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE) return ItemHolder.of(stack.getItem());
        else if (stack.getItem() instanceof ItemSizeable) return SizeableHolder.of(HFCore.SIZEABLE.getObjectFromStack(stack));
        else if (stack.getItem() instanceof ICropProvider) return CropHolder.of((Crop)((ICropProvider)stack.getItem()).getCrop(stack));
        else if (stack.getItem() instanceof ItemHFFML) return FMLHolder.of(stack);
        else return ItemStackHolder.of(stack);
    }
}
