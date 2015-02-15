package joshie.harvestmoon.npc.gift;

import joshie.harvestmoon.init.HMBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GiftsGoddess extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Item.getItemFromBlock(HMBlocks.flowers)) {
            return Quality.AWESOME;
        }

        if (is(stack, Category.PRETTY)) {
            return Quality.GOOD;
        }

        if (is(stack, Category.CHEAP)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}
