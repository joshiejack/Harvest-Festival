package joshie.harvestmoon.npc.gift;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GiftsJacob extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Items.fishing_rod) {
            return Quality.AWESOME;
        }

        if (is(stack, Category.WATER)) {
            return Quality.GOOD;
        }

        if (is(stack, Category.MINING)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}
