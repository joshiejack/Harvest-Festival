package joshie.harvest.crops.handlers.drop;

import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.crops.DropHandler;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Random;

@SuppressWarnings("unused")
public class DropHandlerNetherWart extends DropHandler {
    @Override
    @Nonnull
    public ItemStack getDrop(Crop crop, int stage, Random rand) {
        return stage >= crop.getStages() ? new ItemStack(Items.NETHER_WART, 2 + rand.nextInt(3)) : ItemStack.EMPTY;
    }
}