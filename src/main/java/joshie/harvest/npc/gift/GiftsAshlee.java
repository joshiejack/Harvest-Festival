package joshie.harvest.npc.gift;

import joshie.harvest.api.npc.gift.GiftCategory;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GiftsAshlee extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Items.EMERALD) {
            return Quality.AWESOME;
        }

        if (GiftRegistry.is(stack, GiftCategory.TECHNOLOGY)) {
            return Quality.GOOD;
        }

        if (GiftRegistry.is(stack, GiftCategory.DANGER)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}
