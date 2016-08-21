package joshie.harvest.npc.gift;

import joshie.harvest.api.npc.gift.GiftCategory;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GiftsJacob extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Items.FISHING_ROD) {
            return Quality.AWESOME;
        }

        if (isGiftType(stack, GiftCategory.WATERY)) {
            return Quality.GOOD;
        }

        if (isGiftType(stack, GiftCategory.MINING)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}