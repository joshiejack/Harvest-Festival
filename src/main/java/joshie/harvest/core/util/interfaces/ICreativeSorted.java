package joshie.harvest.core.util.interfaces;

import joshie.harvest.core.lib.CreativeSort;
import net.minecraft.item.ItemStack;

public interface ICreativeSorted {
    default int getSortValue(ItemStack stack) {
        return CreativeSort.NONE;
    }
}