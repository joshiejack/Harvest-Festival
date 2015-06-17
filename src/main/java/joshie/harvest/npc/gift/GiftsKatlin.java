package joshie.harvest.npc.gift;

import static joshie.harvest.npc.gift.Gifts.Quality.AWESOME;
import static joshie.harvest.npc.gift.Gifts.Quality.BAD;
import static joshie.harvest.npc.gift.Gifts.Quality.DECENT;
import static joshie.harvest.npc.gift.Gifts.Quality.GOOD;
import joshie.harvest.items.HFItems;
import net.minecraft.item.ItemStack;

public class GiftsKatlin extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == HFItems.wool) {
            return AWESOME;
        }
        
        if(is(stack, Category.KNITTING)) {
            return GOOD;
        }

        if (is(stack, Category.TECHNOLOGY)) {
            return BAD;
        }

        return DECENT;
    }
}
