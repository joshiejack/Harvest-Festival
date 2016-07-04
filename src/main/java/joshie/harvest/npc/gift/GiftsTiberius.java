package joshie.harvest.npc.gift;

import joshie.harvest.api.npc.gift.GiftCategory;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.IGiftHandler.Quality.*;

public class GiftsTiberius extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        //If we are strength two potion!
        if (stack.getItem() == Items.POTIONITEM) {
            return stack.getItemDamage() == 8233 ? AWESOME : GOOD;
        }

        if (GiftRegistry.is(stack, GiftCategory.BATTLE)) {
            return Quality.GOOD;
        }

        if (GiftRegistry.is(stack, GiftCategory.GIRLY)) {
            return Quality.BAD;
        }

        return DECENT;
    }
}
