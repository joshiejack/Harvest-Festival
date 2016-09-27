package joshie.harvest.api.crops;

import net.minecraft.item.ItemStack;

import java.util.Random;

public class DropHandler {
    /** Return the item dropped, return null if the item was the default for this crop
     * @param crop      the crop
     * @param stage     the stage
     * @param rand      the rand**/
    public ItemStack getDrop(Crop crop, int stage, Random rand) {
        return stage >= crop.getStages() ? crop.getCropStack(1): null;
    }
}
