package joshie.harvest.api.crops;

import java.util.Random;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IDropHandler {
    /** Return the item dropped, return null if the item was the default for this crop **/
    public ItemStack getDrop(Random rand, Item item);
}
