package joshie.harvest.crops.handlers.drop;

import joshie.harvest.api.crops.IDropHandler;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class DropHandlerPotato implements IDropHandler {
    @Override
    public ItemStack getDrop(Random rand, Item item) {
        return rand.nextInt(50) == 0 ? new ItemStack(Items.POISONOUS_POTATO) : new ItemStack(item);
    }
}