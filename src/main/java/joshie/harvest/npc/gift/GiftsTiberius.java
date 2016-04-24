package joshie.harvest.npc.gift;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static joshie.harvest.npc.gift.Gifts.Quality.*;

public class GiftsTiberius extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        //If we are strength two potion!
        if (stack.getItem() == Items.POTIONITEM) {
            return stack.getItemDamage() == 8233 ? AWESOME : GOOD;
        }

        if (is(stack, Category.BATTLE)) {
            return Quality.GOOD;
        }

        if (is(stack, Category.GIRLY)) {
            return Quality.BAD;
        }

        return DECENT;
    }
}
