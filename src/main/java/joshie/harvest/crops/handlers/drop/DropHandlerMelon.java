package joshie.harvest.crops.handlers.drop;

import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.crops.DropHandler;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class DropHandlerMelon extends DropHandler {
    @Override
    public ItemStack getDrop(Crop crop, int stage, Random rand) {
        return stage >= crop.getStages() ? new ItemStack(Items.MELON, 3 + rand.nextInt(5)): null;
    }
}