package joshie.harvest.npc.gift;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GiftsJim extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Items.STICK) {
            return Quality.AWESOME;
        }

        if (is(stack, Category.CHEAP)) {
            return Quality.GOOD;
        }

        if (is(stack, Category.RARE)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}
