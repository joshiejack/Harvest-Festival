package joshie.harvest.core.helpers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class InventoryHelper {
    public static int getCount(EntityPlayer player, ItemStack stack) {
        int count = 0;
        for (ItemStack item: player.inventory.mainInventory) {
            if (item == null) continue;
            if (item.isItemEqual(stack)) {
                count += item.stackSize;
            }
        }
        
        return count;
    }
    
    public static int getCount(EntityPlayer player, String ore) {
        int count = 0;
        for (ItemStack item: player.inventory.mainInventory) {
            if (item == null) continue;
            int[] ids = OreDictionary.getOreIDs(item);
            for (int i: ids) {
                String name = OreDictionary.getOreName(i);
                if (name.equals(ore)) {
                    count += item.stackSize;
                    break;
                }
            }
        }
        
        return count;
    }
}
