package joshie.harvest.api.crops;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Random;

public interface IDropHandler {
    /** Return the item dropped, return null if the item was the default for this crop **/
    ItemStack getDrop(Random rand, Item item);
}