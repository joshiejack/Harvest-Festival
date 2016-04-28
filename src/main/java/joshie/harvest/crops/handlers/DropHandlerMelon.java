package joshie.harvest.crops.handlers;

import joshie.harvest.api.crops.IDropHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class DropHandlerMelon implements IDropHandler {
    @Override
    public ItemStack getDrop(Random rand, Item item) {
        ItemStack melon = new ItemStack(item);
        melon.stackSize = 3 + rand.nextInt(5);
        return melon;
    }
}