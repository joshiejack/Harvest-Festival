package joshie.harvest.api.fishing;

import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Random;

/** Implement this on items that can have different weight categories **/
public interface IWeightedItem {
    /** Return this item as an item that would be considered this size,
     *  with a random weight value
     * @param rand the random object
     * @param held  the player is holder
     * @param stack the default stack  */
    ItemStack getInWeightRange(Random rand, @Nullable ItemStack held, ItemStack stack);
}
