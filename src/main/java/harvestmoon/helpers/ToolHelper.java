package harvestmoon.helpers;

import harvestmoon.init.HMItems;
import harvestmoon.items.ItemGeneral;
import net.minecraft.item.ItemStack;

public class ToolHelper {
    public static boolean isMilker(ItemStack stack) {
        return stack.getItem() == HMItems.general && stack.getItemDamage() == ItemGeneral.MILKER;
    }

    public static boolean isBrush(ItemStack stack) {
        return stack.getItem() == HMItems.general && stack.getItemDamage() == ItemGeneral.BRUSH;
    }
}
