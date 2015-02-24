package joshie.harvestmoon.core.helpers;

import joshie.harvestmoon.init.HMItems;
import joshie.harvestmoon.items.ItemGeneral;
import net.minecraft.item.ItemStack;

public class ToolHelper {
    public static boolean isMilker(ItemStack stack) {
        return stack.getItem() == HMItems.general && stack.getItemDamage() == ItemGeneral.MILKER;
    }

    public static boolean isBrush(ItemStack stack) {
        return stack.getItem() == HMItems.general && stack.getItemDamage() == ItemGeneral.BRUSH;
    }

    public static boolean isBlueFeather(ItemStack stack) {
        return stack.getItem() == HMItems.general && stack.getItemDamage() == ItemGeneral.BLUE_FEATHER;
    }
}
