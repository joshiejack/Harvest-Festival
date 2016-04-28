package joshie.harvest.npc.gift;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GiftsBrandon extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Items.IRON_PICKAXE) {
            return Quality.AWESOME;
        }

        if (is(stack, Category.MINING)) {
            return Quality.GOOD;
        }

        if (is(stack, Category.NATURE)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}