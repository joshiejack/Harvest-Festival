package joshie.harvest.npc.gift;

import joshie.harvest.items.HFItems;
import net.minecraft.item.ItemStack;

import static joshie.harvest.npc.gift.Gifts.Quality.*;

public class GiftsKatlin extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == HFItems.WOOL) {
            return AWESOME;
        }

        if (is(stack, Category.KNITTING)) {
            return GOOD;
        }

        if (is(stack, Category.TECHNOLOGY)) {
            return BAD;
        }

        return DECENT;
    }
}