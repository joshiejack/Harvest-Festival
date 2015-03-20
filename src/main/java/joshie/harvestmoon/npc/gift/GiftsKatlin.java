package joshie.harvestmoon.npc.gift;

import static joshie.harvestmoon.npc.gift.Gifts.Quality.AWESOME;
import static joshie.harvestmoon.npc.gift.Gifts.Quality.BAD;
import static joshie.harvestmoon.npc.gift.Gifts.Quality.DECENT;
import static joshie.harvestmoon.npc.gift.Gifts.Quality.GOOD;
import joshie.harvestmoon.init.HMItems;
import net.minecraft.item.ItemStack;

public class GiftsKatlin extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == HMItems.yarn) {
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
