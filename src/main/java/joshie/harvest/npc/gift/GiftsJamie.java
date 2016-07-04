package joshie.harvest.npc.gift;

import joshie.harvest.api.npc.gift.GiftCategory;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GiftsJamie extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Items.DIAMOND) {
            return Quality.AWESOME;
        }

        if (GiftRegistry.is(stack, GiftCategory.RARE)) {
            return Quality.GOOD;
        }

        if (GiftRegistry.is(stack, GiftCategory.ANIMALS)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}