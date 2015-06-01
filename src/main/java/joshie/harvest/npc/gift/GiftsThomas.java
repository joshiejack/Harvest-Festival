package joshie.harvest.npc.gift;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GiftsThomas extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Item.getItemFromBlock(Blocks.tnt)) {
            return Quality.AWESOME;
        }

        if (is(stack, Category.DANGER)) {
            return Quality.GOOD;
        }

        if (is(stack, Category.BATTLE)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}
