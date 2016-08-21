package joshie.harvest.npc.gift;

import joshie.harvest.api.npc.gift.GiftCategory;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GiftsJade extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Items.WHEAT_SEEDS) {
            return Quality.AWESOME;
        }

        if (isGiftType(stack, GiftCategory.FARMING)) {
            return Quality.GOOD;
        }

        if (isGiftType(stack, GiftCategory.MINING)) {
            return Quality.BAD;
        }
        return Quality.DECENT;
    }
}