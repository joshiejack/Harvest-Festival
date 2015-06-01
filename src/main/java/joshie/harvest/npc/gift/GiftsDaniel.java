package joshie.harvest.npc.gift;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GiftsDaniel extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Items.ender_pearl) {
            return Quality.AWESOME;
        }

        if (is(stack, Category.TOOLS)) {
            return Quality.GOOD;
        }

        if (is(stack, Category.WATER)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}
