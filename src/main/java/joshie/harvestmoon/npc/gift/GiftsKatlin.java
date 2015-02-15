package joshie.harvestmoon.npc.gift;

import static joshie.harvestmoon.npc.gift.Gifts.Quality.*;
import static joshie.harvestmoon.npc.gift.Gifts.Quality.DECENT;
import static joshie.harvestmoon.npc.gift.Gifts.Quality.GOOD;
import static joshie.harvestmoon.npc.gift.Gifts.Quality.TERRIBLE;
import joshie.harvestmoon.helpers.SizeableHelper;
import joshie.harvestmoon.init.HMItems;
import joshie.harvestmoon.lib.SizeableMeta;
import net.minecraft.item.ItemStack;

public class GiftsKatlin extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == HMItems.sized) {
            SizeableMeta meta = SizeableHelper.getSizeableFromStack(stack);
            return meta == SizeableMeta.YARN ? AWESOME : GOOD;
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
