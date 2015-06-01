package joshie.harvest.npc.gift;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GiftsAbi extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Items.sugar) {
            return Quality.AWESOME;
        }

        if (is(stack, Category.CUTE)) {
            return Quality.GOOD;
        }

        if (is(stack, Category.SCARY)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}
