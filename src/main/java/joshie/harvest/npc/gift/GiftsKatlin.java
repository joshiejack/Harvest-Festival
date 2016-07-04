package joshie.harvest.npc.gift;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.npc.gift.GiftCategory;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.IGiftHandler.Quality.*;

public class GiftsKatlin extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == HFAnimals.WOOL) {
            return AWESOME;
        }

        if (GiftRegistry.is(stack, GiftCategory.KNITTING)) {
            return GOOD;
        }

        if (GiftRegistry.is(stack, GiftCategory.TECHNOLOGY)) {
            return BAD;
        }

        return DECENT;
    }
}