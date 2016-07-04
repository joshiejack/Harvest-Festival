package joshie.harvest.npc.gift;

import joshie.harvest.api.npc.gift.GiftCategory;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GiftsCloe extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Items.BONE) {
            return Quality.AWESOME;
        }

        if (GiftRegistry.is(stack, GiftCategory.SCARY)) {
            return Quality.GOOD;
        }

        if (GiftRegistry.is(stack, GiftCategory.CUTE)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}