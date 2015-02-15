package joshie.harvestmoon.npc.gift;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GiftsJade extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Items.wheat_seeds) {
            return Quality.AWESOME;
        }

        if (is(stack, Category.FARMING)) {
            return Quality.GOOD;
        }

        if (is(stack, Category.MINING)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}
