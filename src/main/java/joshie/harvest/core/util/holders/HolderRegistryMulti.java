package joshie.harvest.core.util.holders;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.Mod;
import joshie.harvest.api.core.Ore;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;

public class HolderRegistryMulti<R> {
    private final HashMap<AbstractItemHolder, R> registry = new HashMap<>();
    private final Multimap<Item, AbstractItemHolder> keyMap = HashMultimap.create();

    public void removeItem(Item item) {
        keyMap.removeAll(item);
    }

    private void registerHolder(Item item, AbstractItemHolder holder, R r) {
        keyMap.get(item).add(holder);
        registry.put(holder, r);
    }

    public void register(Object object, R r) {
        if (object instanceof Item) registerHolder((Item) object, ItemHolder.of((Item) object), r);
        if (object instanceof Block) {
            ItemStack stack = new ItemStack((Block) object);
            registerHolder(stack.getItem(), ItemHolder.of(stack.getItem()), r);
        } else if (object instanceof ItemStack) {
            registerHolder(((ItemStack) object).getItem(), getHolder((ItemStack) object), r);
        } else if (object instanceof Mod) {
            Mod mod = (Mod) object;
            ModHolder holder = ModHolder.of(mod.getMod());
            for (Item item : Item.REGISTRY) {
                if (item.getRegistryName().getResourceDomain().equals(mod.getMod())) {
                    registerHolder(item, holder, r);
                }
            }
        } else if (object instanceof Ore) {
            Ore ore = (Ore) object;
            OreHolder holder = OreHolder.of(ore.getOre(), ore.getType());
            for (ItemStack stack : OreDictionary.getOres(ore.getOre())) {
                registerHolder(stack.getItem(), holder, r);
            }
        }
    }

    public boolean matches(@Nonnull ItemStack stack, R type) {
        Collection<AbstractItemHolder> holders = keyMap.get(stack.getItem());
        for (AbstractItemHolder holder : holders) {
            if (holder.matches(stack) && matches(registry.get(holder), type)) {
                return true;
            }
        }

        return false;
    }

    public R getValueOf(@Nonnull ItemStack stack) {
        Collection<AbstractItemHolder> holders = keyMap.get(stack.getItem());
        for (AbstractItemHolder holder : holders) {
            if (holder.matches(stack)) {
                return registry.get(holder);
            }
        }

        return null;
    }

    public NonNullList<ItemStack> getStacksFor(R ingredient) {
        NonNullList<ItemStack> result = NonNullList.create();
        registry.entrySet().stream().filter(entry -> isEqual(ingredient, entry.getValue())).forEach(entry -> result.addAll(entry.getKey().getMatchingStacks()));
        return result;
    }

    public boolean isEqual(R r1, R r2) {
        return true;
    }

    public boolean matches(R internal, R external) {
        return internal == external;
    }

    private AbstractItemHolder getHolder(@Nonnull ItemStack stack) {
        if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE) return ItemHolder.of(stack.getItem());
        else if (HFApi.crops.getCropFromStack(stack) != null) return CropHolder.of(HFApi.crops.getCropFromStack(stack));
        else return ItemStackHolder.of(stack);
    }
}