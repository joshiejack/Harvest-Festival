package joshie.harvestmoon.helpers;

import java.util.HashMap;

import joshie.harvestmoon.init.HMItems;
import joshie.harvestmoon.util.HMStack;
import joshie.harvestmoon.util.SafeStack;
import joshie.harvestmoon.util.WildStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class SafeStackHelper {
    public static SafeStack getSafeStackType(ItemStack stack) {
        if (stack.getItem() == HMItems.crops || stack.getItem() == HMItems.sized) {
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
