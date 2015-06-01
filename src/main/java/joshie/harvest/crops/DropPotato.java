package joshie.harvest.crops;

import java.util.Random;

import joshie.harvest.api.crops.IDropHandler;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DropPotato implements IDropHandler {
    @Override
    public ItemStack getDrop(Random rand, Item item) {
        return rand.nextInt(50) == 0 ? new ItemStack(Items.poisonous_potato) : null;
    }
}
