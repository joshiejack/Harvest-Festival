package joshie.harvest.npc.gift;

import joshie.harvest.api.npc.gift.IGiftHandler;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.GiftCategory.CUTE;
import static joshie.harvest.api.npc.gift.GiftCategory.SCARY;
import static joshie.harvest.api.npc.gift.IGiftHandler.Quality.*;
import static net.minecraft.init.Items.SUGAR;

public class GiftsAbi implements IGiftHandler {
    @Override
    public Quality getQuality(ItemStack stack) {
        return stack.getItem() == SUGAR ? AWESOME : GiftRegistry.is(stack, CUTE) ? GOOD : GiftRegistry.is(stack, SCARY) ? BAD : DECENT;
    }
}