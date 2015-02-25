package joshie.harvestmoon.core.helpers;

import joshie.harvestmoon.init.HMItems;
import joshie.harvestmoon.items.ItemBaseTool;
import joshie.harvestmoon.items.ItemGeneral;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

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

    public static void levelTool(ItemStack stack) {
        if (stack == null) return;
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setDouble("Level", 0d);
        } else {
            double level = stack.getTagCompound().getDouble("Level");
            double increase = ((ItemBaseTool) stack.getItem()).getLevelIncrease(stack);
            double newLevel = Math.min(100D, level + increase);
            stack.getTagCompound().setDouble("Level", newLevel);
        }
    }
}
