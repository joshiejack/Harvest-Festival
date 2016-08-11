package joshie.harvest.npc.gift;

import joshie.harvest.api.npc.gift.GiftCategory;
import joshie.harvest.api.npc.gift.IGiftHandler;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GiftsGirafi implements IGiftHandler {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Items.POTATO) {
            return Quality.AWESOME;
        }

        if (GiftRegistry.is(stack, GiftCategory.ANIMALS) || GiftRegistry.is(stack, GiftCategory.TECHNOLOGY)) {
            return Quality.GOOD;
        }

        if (GiftRegistry.is(stack, GiftCategory.GIRLY) || GiftRegistry.is(stack, GiftCategory.KNITTING)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}