package joshie.harvestmoon.core.helpers;

import java.util.Collection;
import java.util.HashMap;

import joshie.harvestmoon.api.cooking.IMealProvider;
import joshie.harvestmoon.api.core.ISizedProvider;
import joshie.harvestmoon.api.crops.ICropProvider;
import joshie.harvestmoon.core.util.HMStack;
import joshie.harvestmoon.core.util.MealStack;
import joshie.harvestmoon.core.util.SafeStack;
import joshie.harvestmoon.core.util.WildStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.google.common.collect.Multimap;

public class SafeStackHelper {
    public static SafeStack getSafeStackType(ItemStack stack) {
        if (stack.getItem() instanceof IMealProvider) {
            return new MealStack(stack);
        } if (stack.getItem() instanceof ICropProvider || stack.getItem() instanceof ISizedProvider) {
            return new HMStack(stack);
        } else if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
            return new WildStack(stack);
        } else return new SafeStack(stack);
    }
    
    public static Collection getResult(ItemStack stack, Multimap map) {
        Collection list = map.get(new SafeStack(stack));
        if (list.size() < 1) list = map.get(new MealStack(stack));
        if (list.size() < 1) list = map.get(new WildStack(stack));
        if (list.size() < 1) list = map.get(new HMStack(stack));
        return list;
    }
    
    public static Object getResult(ItemStack stack, HashMap map) {
        Object result = map.get(new SafeStack(stack));
        if (result == null) result = map.get(new MealStack(stack));
        if (result == null) result = map.get(new WildStack(stack));
        if (result == null) result = map.get(new HMStack(stack));
        return result;
    }
}
