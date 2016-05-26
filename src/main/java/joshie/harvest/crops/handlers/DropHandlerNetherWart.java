package joshie.harvest.crops.handlers;

import joshie.harvest.api.crops.IDropHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class DropHandlerNetherWart implements IDropHandler {
    @Override
    public ItemStack getDrop(Random rand, Item item) {
        return new ItemStack(item, 2 + rand.nextInt(3));
    }
}