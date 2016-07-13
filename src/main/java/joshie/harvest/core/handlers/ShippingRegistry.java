package joshie.harvest.core.handlers;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.IShippable;
import joshie.harvest.api.core.IShippingRegistry;
import joshie.harvest.api.core.ISizeable;
import joshie.harvest.api.crops.ICrop;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;

public class ShippingRegistry implements IShippingRegistry {
    private final HashMap<Pair<Item, Integer>, Long> registry = new HashMap<>();

    @Override
    public void registerSellable(ItemStack stack, long value) {
        registry.put(Pair.of(stack.getItem(), stack.getItemDamage()), value);
    }

    @Override
    public long getSellValue(ItemStack stack) {
        if (stack.getItem() instanceof IShippable) {
            return ((IShippable)stack.getItem()).getSellValue(stack);
        }

        //Special case Crops
        ICrop crop = HFApi.crops.getCropFromStack(stack);
        if (crop != null) return crop.getSellValue(stack);

        //Special case Sizeables
        ISizeable sizeable = HFApi.sizeable.getSizeable(stack);
        if (sizeable != null) return sizeable.getValue(HFApi.sizeable.getSize(stack));

        //Wildcard
        Long value = registry.get(Pair.of(stack.getItem(), OreDictionary.WILDCARD_VALUE));
        if (value != null) return value;

        //Normal
        value = registry.get(Pair.of(stack.getItem(), stack.getItemDamage()));
        if (value != null) return value;

        return 0;
    }
}
