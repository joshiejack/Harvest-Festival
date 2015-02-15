package joshie.harvestmoon.npc.gift;

import joshie.harvestmoon.init.HMItems;
import joshie.harvestmoon.items.ItemGeneral;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GiftsLiara extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == HMItems.general && stack.getItemDamage() == ItemGeneral.CHOCOLATE) {
            return Quality.AWESOME;
        }

        if (is(stack, Category.COOKING)) {
            return Quality.GOOD;
        }

        if (is(stack, Category.KNITTING)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}
