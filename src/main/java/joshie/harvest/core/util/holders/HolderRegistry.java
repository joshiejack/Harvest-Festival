package joshie.harvest.core.util.holders;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.Mod;
import joshie.harvest.api.core.Ore;
import joshie.harvest.core.util.interfaces.IFMLItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

public class HolderRegistry<R> {
    private final LinkedHashMap<AbstractItemHolder, R> registry = new LinkedHashMap<>();

    public void register(Object object, R r) {
        if (object instanceof Item) {
            registry.put(ItemHolder.of((Item) object), r);
        } else if (object instanceof Block) {
            registry.put(ItemHolder.of(new ItemStack((Block)object).getItem()), r);
        } else if (object instanceof ItemStack) {
            registry.put(getHolder((ItemStack)object), r);
        } else if (object instanceof Mod) {
            registry.put(ModHolder.of(((Mod) object).getMod()), r);
        } else if (object instanceof Ore) {
            registry.put(OreHolder.of(((Ore) object).getOre()), r);
        }
    }

    public boolean matches(ItemStack stack, R type) {
        R value = getValueOf(stack);
        return value != null && matches(value, type);
    }

    public R getValue(AbstractItemHolder holder) {
        return registry.get(holder);
    }

    public R getValueOf(ItemStack stack) {
        for (Entry<AbstractItemHolder, R> entry: registry.entrySet()) {
            if (entry.getKey().matches(stack)) return entry.getValue();
        }

        return null;
    }

    public List<ItemStack> getStacksFor(R ingredient) {
        ArrayList<ItemStack> result = new ArrayList<>();
        registry.entrySet().stream().filter(entry -> matches(ingredient, entry.getValue())).forEach(entry -> result.addAll(entry.getKey().getMatchingStacks()));
        return result;
    }

    public List<AbstractItemHolder> getStacks() {
        return new ArrayList<>(registry.keySet());
    }

    public boolean matches(R external, R internal) {
        return external == internal;
    }

    private AbstractItemHolder getHolder(ItemStack stack) {
        if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE) return ItemHolder.of(stack.getItem());
        else if (HFApi.crops.getCropFromStack(stack) != null) return CropHolder.of(HFApi.crops.getCropFromStack(stack));
        else if (stack.getItem() instanceof IFMLItem) return FMLHolder.of(stack);
        else return ItemStackHolder.of(stack);
    }
}
