package joshie.harvestmoon.core.helpers;

import java.util.HashMap;

import joshie.harvestmoon.api.crops.ICropProvider;
import joshie.harvestmoon.core.util.HMStack;
import joshie.harvestmoon.core.util.SafeStack;
import joshie.harvestmoon.core.util.WildStack;
import joshie.harvestmoon.init.HMItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class SafeStackHelper {
    public static SafeStack getSafeStackType(ItemStack stack) {
        if (stack.getItem() instanceof ICropProvider || stack.getItem() == HMItems.sized) {
            return new HMStack(stack);
        } else if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
            return new WildStack(stack);
        } else return new SafeStack(stack);
    }
    
    public static Object getResult(ItemStack stack, HashMap map) {
        Object result = map.get(new SafeStack(stack));
        if (result == null) result = map.get(new WildStack(stack));
        if (result == null) result = map.get(new HMStack(stack));
        return result;
    }
}
