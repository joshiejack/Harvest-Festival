package joshie.harvest.core.helpers;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.core.util.base.ItemBaseTool;
import joshie.harvest.npc.HFNPCs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import static joshie.harvest.animals.item.ItemAnimalTool.Tool.BRUSH;
import static joshie.harvest.animals.item.ItemAnimalTool.Tool.MILKER;
import static joshie.harvest.cooking.items.ItemIngredients.Ingredient.OIL;
import static joshie.harvest.npc.items.ItemNPCTool.NPCTool.BLUE_FEATHER;

public class ToolHelper {
    public static boolean isMilker(ItemStack stack) {
        return HFAnimals.TOOLS.getEnumFromStack(stack) == MILKER;
    }

    public static boolean isBrush(ItemStack stack) {
        return HFAnimals.TOOLS.getEnumFromStack(stack) == BRUSH;
    }

    public static boolean isBlueFeather(ItemStack stack) {
        return HFNPCs.TOOLS.getEnumFromStack(stack) == BLUE_FEATHER;
    }

    public static boolean isEgg(ItemStack heldItem) {
        return heldItem.getItem() == HFAnimals.EGG;
    }

    public static boolean isOil(ItemStack stack) {
        return HFCooking.INGREDIENTS.getEnumFromStack(stack) == OIL;
    }

    public static void levelTool(ItemStack stack) {
        if (stack == null) return;
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setDouble("Level", 0D);
        } else {
            double level = stack.getTagCompound().getDouble("Level");
            double increase = ((ItemBaseTool) stack.getItem()).getLevelIncrease(stack);
            double newLevel = Math.min(100D, level + increase);
            stack.getTagCompound().setDouble("Level", newLevel);
        }
    }
}
