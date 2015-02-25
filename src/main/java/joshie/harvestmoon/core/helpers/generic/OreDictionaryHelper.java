package joshie.harvestmoon.core.helpers.generic;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryHelper {
    public static boolean isLogs(Block block) {
        for (String name : getNames(new ItemStack(block))) {
            if (name.equals("logWood")) return true;
        }

        return false;
    }

    public static String[] getNames(ItemStack stack) {
        int[] ids = OreDictionary.getOreIDs(stack);
        String[] names = new String[ids.length];
        for (int i = 0; i < ids.length; i++) {
            names[i] = OreDictionary.getOreName(ids[i]);
        }

        return names;
    }
}
