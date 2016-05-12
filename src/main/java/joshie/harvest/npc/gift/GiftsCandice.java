package joshie.harvest.npc.gift;

import joshie.harvest.items.HFItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GiftsCandice extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Items.MILK_BUCKET || stack.getItem() == HFItems.MILK) {
            return Quality.AWESOME;
        }

        if (is(stack, Category.ANIMALS)) {
            return Quality.GOOD;
        }

        if (is(stack, Category.COOKING)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}
