package joshie.harvest.core.util.holders;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.Mod;
import joshie.harvest.api.core.Ore;
import joshie.harvest.core.base.item.ItemHFSizeable;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collection;

public class HolderRegistrySet {
    private final Multimap<Item, AbstractItemHolder> setMap = HashMultimap.create();

    private void registerHolder(Item item, AbstractItemHolder holder) {
        setMap.get(item).add(holder);
    }

    private void unregisterHolder(Item item, AbstractItemHolder holder) {
        setMap.remove(item, holder);
        if (setMap.get(item).size() == 0) {
            setMap.removeAll(item);
        }
    }

    public void unregister(Object object) {
        if (object instanceof Item) unregisterHolder((Item)object, ItemHolder.of((Item)object));
        if (object instanceof Block) {
            ItemStack stack = new ItemStack((Block)object);
            unregisterHolder(stack.getItem(), ItemHolder.of(stack.getItem()));
        } else if (object instanceof ItemStack) {
            unregisterHolder(((ItemStack)object).getItem(), getHolder((ItemStack)object));
        } else if (object instanceof Mod) {
            Mod mod = (Mod) object;
            ModHolder holder = ModHolder.of(mod.getMod());
            for (Item item: Item.REGISTRY) {
                if (item.getRegistryName().getResourceDomain().equals(mod.getMod())) {
                    unregisterHolder(item, holder);
                }
            }
        } else if (object instanceof Ore) {
            Ore ore = (Ore) object;
            OreHolder holder = OreHolder.of(ore.getOre());
            for (ItemStack stack: OreDictionary.getOres(ore.getOre())) {
                unregisterHolder(stack.getItem(), holder);
            }
        }
    }

    public void register(Object object) {
        if (object instanceof Item) registerHolder((Item)object, ItemHolder.of((Item)object));
        if (object instanceof Block) {
            ItemStack stack = new ItemStack((Block)object);
            registerHolder(stack.getItem(), ItemHolder.of(stack.getItem()));
        } else if (object instanceof ItemStack) {
            registerHolder(((ItemStack)object).getItem(), getHolder((ItemStack)object));
        } else if (object instanceof Mod) {
            Mod mod = (Mod) object;
            ModHolder holder = ModHolder.of(mod.getMod());
            for (Item item: Item.REGISTRY) {
                if (item.getRegistryName().getResourceDomain().equals(mod.getMod())) {
                    registerHolder(item, holder);
                }
            }
        } else if (object instanceof Ore) {
            Ore ore = (Ore) object;
            OreHolder holder = OreHolder.of(ore.getOre());
            for (ItemStack stack: OreDictionary.getOres(ore.getOre())) {
                registerHolder(stack.getItem(), holder);
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
        else if (stack.getItem() instanceof ItemHFSizeable) return SizeableHolder.of(stack);
        else if (HFApi.crops.getCropFromStack(stack) != null) return CropHolder.of(HFApi.crops.getCropFromStack(stack));
        else return ItemStackHolder.of(stack);
    }
}
