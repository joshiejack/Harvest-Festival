package joshie.harvest.core.util.interfaces;

import joshie.harvest.core.lib.CreativeSort;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface ICreativeSorted {
    default int getSortValue(@Nonnull ItemStack stack) {
        return CreativeSort.NONE;
    }
}