package joshie.harvestmoon.npc.gift;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GiftsOndra extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Items.emerald) {
            return Quality.AWESOME;
        }

        if (is(stack, Category.TECHNOLOGY)) {
            return Quality.GOOD;
        }

        if (is(stack, Category.DANGER)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}
