package joshie.harvest.npc.gift;

import joshie.harvest.api.npc.gift.GiftCategory;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GiftsDaniel extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Items.ENDER_PEARL) {
            return Quality.AWESOME;
        }

        if (isGiftType(stack, GiftCategory.TOOLS)) {
            return Quality.GOOD;
        }

        if (isGiftType(stack, GiftCategory.WATERY)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}