package joshie.harvest.npc.gift;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GiftsJenni extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Items.CARROT_ON_A_STICK) {
            return Quality.AWESOME;
        }

        if (is(stack, Category.GIRLY)) {
            return Quality.GOOD;
        }

        if (is(stack, Category.TOOLS)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}