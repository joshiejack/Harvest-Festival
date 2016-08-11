package joshie.harvest.npc.gift;

import joshie.harvest.api.npc.gift.GiftCategory;
import joshie.harvest.api.npc.gift.IGiftHandler;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GiftsGirafi implements IGiftHandler {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Items.WHEAT) {
            return Quality.AWESOME;
        }

        if (GiftRegistry.is(stack, GiftCategory.ANIMALS)) {
            return Quality.GOOD;
        }

        if (GiftRegistry.is(stack, GiftCategory.CONSTRUCTION)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}