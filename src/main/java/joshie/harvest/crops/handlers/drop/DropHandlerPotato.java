package joshie.harvest.crops.handlers.drop;

import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.crops.DropHandler;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.Random;

@SuppressWarnings("unused")
public class DropHandlerPotato extends DropHandler {
    @Override
    public ItemStack getDrop(Crop crop, int stage, Random rand) {
        return stage >= crop.getStages() ? rand.nextInt(50) == 0 ? new ItemStack(Items.POISONOUS_POTATO) : new ItemStack(Items.POTATO) : null;
    }
}