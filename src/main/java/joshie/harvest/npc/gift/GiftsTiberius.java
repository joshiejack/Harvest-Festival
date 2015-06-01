package joshie.harvest.npc.gift;

import static joshie.harvest.npc.gift.Gifts.Quality.AWESOME;
import static joshie.harvest.npc.gift.Gifts.Quality.DECENT;
import static joshie.harvest.npc.gift.Gifts.Quality.GOOD;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GiftsTiberius extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        //If we are strength two potion!
        if (stack.getItem() == Items.potionitem) {
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
