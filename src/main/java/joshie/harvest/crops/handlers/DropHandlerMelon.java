package joshie.harvest.crops.handlers;

import java.util.Random;

import joshie.harvest.api.crops.IDropHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DropHandlerMelon implements IDropHandler {
    @Override
    public ItemStack getDrop(Random rand, Item item) {
        ItemStack melon = new ItemStack(item);
        melon.stackSize = 3 + rand.nextInt(5);
        return melon;
    }
}
