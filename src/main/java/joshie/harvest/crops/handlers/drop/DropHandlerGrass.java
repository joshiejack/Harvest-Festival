package joshie.harvest.crops.handlers.drop;

import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.crops.DropHandler;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Random;

@SuppressWarnings("unused")
public class DropHandlerGrass extends DropHandler {
    @Override
    @Nonnull
    public ItemStack getDrop(Crop crop, int stage, Random rand) {
        return stage >= crop.getMinimumCut() ? crop.getCropStack(1 + (stage - crop.getMinimumCut())) : ItemStack.EMPTY;
    }
}