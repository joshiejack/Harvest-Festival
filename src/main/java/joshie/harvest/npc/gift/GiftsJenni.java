package joshie.harvest.npc.gift;

import joshie.harvest.api.npc.gift.GiftCategory;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GiftsJenni extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Items.CARROT_ON_A_STICK) {
            return Quality.AWESOME;
        }

        if (GiftRegistry.is(stack, GiftCategory.GIRLY)) {
            return Quality.GOOD;
        }

        if (GiftRegistry.is(stack, GiftCategory.TOOLS)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}