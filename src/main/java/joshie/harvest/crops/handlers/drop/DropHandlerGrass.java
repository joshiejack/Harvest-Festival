package joshie.harvest.crops.handlers.drop;

import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.crops.DropHandler;
import net.minecraft.item.ItemStack;

import java.util.Random;

@SuppressWarnings("unused")
public class DropHandlerGrass extends DropHandler {
    @Override
    public ItemStack getDrop(Crop crop, int stage, Random rand) {
        return stage >= crop.getMinimumCut() ? crop.getCropStack((int) Math.floor(stage / 2) - 2) : null;
    }
}