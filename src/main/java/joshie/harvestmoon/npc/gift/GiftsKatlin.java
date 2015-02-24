package joshie.harvestmoon.npc.gift;

import static joshie.harvestmoon.npc.gift.Gifts.Quality.*;
import joshie.harvestmoon.core.helpers.SizeableHelper;
import joshie.harvestmoon.core.lib.SizeableMeta;
import joshie.harvestmoon.init.HMItems;
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
