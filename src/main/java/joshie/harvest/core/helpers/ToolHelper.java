package joshie.harvest.core.helpers;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.items.HFItems;
import joshie.harvest.items.ItemBaseTool;
import joshie.harvest.items.ItemGeneral;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import static joshie.harvest.animals.item.ItemAnimalTool.Tool.BRUSH;
import static joshie.harvest.animals.item.ItemAnimalTool.Tool.MILKER;

public class ToolHelper {
    public static boolean isMilker(ItemStack stack) {
        return HFAnimals.TOOLS.getEnumFromStack(stack) == MILKER;
    }

    public static boolean isBrush(ItemStack stack) {
        return HFAnimals.TOOLS.getEnumFromStack(stack) == BRUSH;
    }

    public static boolean isBlueFeather(ItemStack stack) {
        return stack.getItem() == HFItems.GENERAL && stack.getItemDamage() == ItemGeneral.BLUE_FEATHER;
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

    public static boolean isEgg(ItemStack heldItem) {
        return heldItem.getItem() == HFAnimals.EGG;
    }
}
