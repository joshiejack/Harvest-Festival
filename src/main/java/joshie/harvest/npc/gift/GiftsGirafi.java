package joshie.harvest.npc.gift;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

public class GiftsGirafi extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Items.POTATO) {
            return Quality.AWESOME;
        }

        if (isGiftType(stack, ANIMALS, TECHNOLOGY)) {
            return Quality.GOOD;
        }

        if (isGiftType(stack, GIRLY, KNITTING)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}