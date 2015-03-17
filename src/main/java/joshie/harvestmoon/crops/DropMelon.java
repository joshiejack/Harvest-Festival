package joshie.harvestmoon.crops;

import java.util.Random;

import joshie.harvestmoon.api.crops.IDropHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DropMelon implements IDropHandler {
    @Override
    public ItemStack getDrop(Random rand, Item item, int quality) {
        ItemStack melon = new ItemStack(item, 1, quality);
        melon.stackSize = 3 + rand.nextInt(5);
        return melon;
    }
}
