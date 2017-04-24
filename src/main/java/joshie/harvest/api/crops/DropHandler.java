package joshie.harvest.api.crops;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.Random;

public class DropHandler<C extends Crop> {
    /** Return the item dropped, return null if the item was the default for this crop
     * @param crop      the crop
     * @param stage     the stage
     * @param rand      the rand**/
    public ItemStack getDrop(C crop, int stage, Random rand) {
        return stage >= crop.getStages() ? crop.getCropStack(1): ItemStack.EMPTY;
    }

    /** Return a list of drops
         * @param crop      the crop
         * @param stage     the stage
         * @param rand      the rand **/
    public NonNullList<ItemStack> getDrops(C crop, int stage, Random rand) {
        NonNullList<ItemStack> list = NonNullList.create();
        ItemStack stack = getDrop(crop, stage, rand);
        if (stack != null) list.add(stack);
        return list;
    }
}
