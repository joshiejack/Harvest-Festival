package joshie.harvest.npc.gift;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GiftsJacob extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Items.FISHING_ROD) {
            return Quality.AWESOME;
        }

        if (is(stack, Category.WATERY)) {
            return Quality.GOOD;
        }

        if (is(stack, Category.MINING)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}