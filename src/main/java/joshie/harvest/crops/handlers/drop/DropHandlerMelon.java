package joshie.harvest.crops.handlers.drop;

import joshie.harvest.api.crops.IDropHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class DropHandlerMelon implements IDropHandler {
    @Override
    public ItemStack getDrop(Random rand, Item item) {
        return new ItemStack(item, 3 + rand.nextInt(5));
    }
}