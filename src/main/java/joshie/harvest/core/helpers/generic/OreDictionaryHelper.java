package joshie.harvest.core.helpers.generic;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryHelper {
    public static boolean isLogs(ItemStack stack) {
        for (String name : getNames(stack)) {
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

    public static boolean isFlowers(ItemStack held) {
        return held.getItem() == Item.getItemFromBlock(Blocks.red_flower);
    }
}
