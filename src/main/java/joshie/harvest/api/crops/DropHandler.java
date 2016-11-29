package joshie.harvest.api.crops;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DropHandler<C extends Crop> {
    /** Return the item dropped, return null if the item was the default for this crop
     * @param crop      the crop
     * @param stage     the stage
     * @param rand      the rand**/
    public ItemStack getDrop(C crop, int stage, Random rand) {
        return stage >= crop.getStages() ? crop.getCropStack(1): null;
    }

    /** Return a list of drops
         * @param crop      the crop
         * @param stage     the stage
         * @param rand      the rand **/
    public List<ItemStack> getDrops(C crop, int stage, Random rand) {
        List<ItemStack> list = new ArrayList<>();
        ItemStack stack = getDrop(crop, stage, rand);
        if (stack != null) list.add(stack);
        return list;
    }
}
